package ru.mint.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.configureSecurity() {

    install(Authentication) {
        jwt {
            realm = "mint"
            verifier(makeJwtVerifier(newJWTOptions(environment)))
            validate { credential ->
                JWTPrincipal(credential.payload)
//                TODO: add audience subject when create JWT/ validate this subject
//                credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }

    install(AuthorityBasedAuthorization) {

    }

}

private fun makeJwtVerifier(options: JWTOptions): JWTVerifier = JWT
    .require(options.algorithm)
    .withSubject("Authentication")
    .withIssuer(options.issuer)
    .build()

private fun newJWTOptions(environment: ApplicationEnvironment): JWTOptions {
    val jwtConfig = environment.config.config("application.JWT")
    val secret = jwtConfig.property("secret").getString()
    val issuer = jwtConfig.property("issuer").getString()
    val validityInMs = jwtConfig.property("validityInMs").getString().toInt()
    return JWTOptions(secret, issuer, validityInMs)
}
