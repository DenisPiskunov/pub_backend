package ru.mint.plugins

import io.ktor.application.*
import ru.mint.service.JWTOptions

fun newJWTOptions(environment: ApplicationEnvironment): JWTOptions {
    val  jwtConfig = environment.config.config("application.JWT")
    val secret = jwtConfig.property("secret").getString()
    val issuer = jwtConfig.property("issuer").getString()
    val validityInMs = jwtConfig.property("validityInMs").getString().toInt()
    return JWTOptions(secret, issuer, validityInMs)
}