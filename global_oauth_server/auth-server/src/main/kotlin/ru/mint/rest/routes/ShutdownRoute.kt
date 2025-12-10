package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.mint.rest.model.SignInRequest
import ru.mint.rest.security.AuthResult
import ru.mint.rest.security.AuthenticationService
import ru.mint.rest.security.RefreshTokenCookieFactory

fun Route.shutdownServer() {

    val shutdown = ShutDownUrl("") { 1 }

    get("/shutdown") {
        shutdown.doShutdown(call)
    }
}