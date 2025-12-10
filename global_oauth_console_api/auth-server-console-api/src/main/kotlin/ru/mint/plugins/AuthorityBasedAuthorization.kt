package ru.mint.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.Authentication.*
import io.ktor.auth.jwt.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.mint.dao.Authorities
import ru.mint.service.AccountService
import java.util.*

//TODO: move to plugins folder when it will be created

fun Route.hasAuthority(authority: Authorities, build: Route.() -> Unit): Route {
    val description = "has authority : ${authority.name}"
    val authorizedRoute = createChild(AuthorizedRouteSelector(description))
    application.feature(AuthorityBasedAuthorization).interceptPipeline(authorizedRoute, authority)
    authorizedRoute.build()
    return authorizedRoute
}

class AuthorizedRouteSelector(private val description: String) : RouteSelector(RouteSelectorEvaluation.qualityConstant) {

    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize ${description})"
}

class AuthorityBasedAuthorization(config: Configuration) {

    fun interceptPipeline(pipeline: ApplicationCallPipeline, authority: Authorities) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val accountUUID = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("uuid").asString())
            val accountService by closestDI().instance<AccountService>()
            val authorities = accountService.findAuthorities(accountUUID).map { it.name }
            if (!authorities.contains(authority)) {
                throw AuthorizationException("Access denied. Account with uuid = $accountUUID has not ${authority.name} authority.")
            }
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, AuthorityBasedAuthorization> {

        override val key = AttributeKey<AuthorityBasedAuthorization>("AuthorityBasedAuthorization")
        val AuthorizationPhase = PipelinePhase("Authorization")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): AuthorityBasedAuthorization {
            val configuration = Configuration().apply(configure)
            return AuthorityBasedAuthorization(configuration)
        }
    }
}

class AuthorizationException(override val message: String) : Exception(message)