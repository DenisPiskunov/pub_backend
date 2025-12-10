package ru.mint.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import ru.mint.plugins.AuthorizationException

fun Application.configureExceptionHandling() {

    install(StatusPages) {
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
            call.application.log.error(cause.message)
        }
    }

}