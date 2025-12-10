package ru.mint.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import ru.mint.service.JWTOptions

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            realm = "mint"
//            TODO: duplicate creation of JWTOptions
            verifier(makeJwtVerifier(newJWTOptions(environment)))
            validate { credential ->
                JWTPrincipal(credential.payload)
//                TODO: add audience subject when create JWT/ validate this subject
//                credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

private fun makeJwtVerifier(options: JWTOptions): JWTVerifier = JWT
    .require(options.algorithm)
    .withSubject("Authentication")
    .withIssuer(options.issuer)
    .build()

