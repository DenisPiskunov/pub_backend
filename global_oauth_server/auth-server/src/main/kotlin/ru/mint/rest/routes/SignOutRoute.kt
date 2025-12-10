package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.mint.rest.model.SignOutRequest
import ru.mint.rest.security.SignOutService
import ru.mint.rest.security.SignOutService.Session
import java.util.*

fun Route.signOut() {

    val signOutService by closestDI().instance<SignOutService>()

    post("sign-out") {
        val refreshTokenCookie = call.request.cookies["refreshToken"]
        if (refreshTokenCookie.isNullOrBlank()) {
            call.application.environment.log.error("Refresh token is not presented in request!")
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            //            TODO: refactor, create method to obtain account UUID
            val accountUUID = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("uuid").asString())
            signOutService.signOut(accountUUID, refreshTokenCookie, getSessionParam(call))
            call.respond(HttpStatusCode.OK)
        }
    }

}

private suspend fun getSessionParam(call: ApplicationCall): Session {
    val sessionParam = call.receive<SignOutRequest>().session
    return Session.values().firstOrNull { it.name == sessionParam.uppercase() } ?: Session.CURRENT
}