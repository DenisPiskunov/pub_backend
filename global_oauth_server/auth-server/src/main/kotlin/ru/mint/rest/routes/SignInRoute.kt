package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.mint.rest.model.SignInRequest
import ru.mint.rest.security.AuthResult.*
import ru.mint.rest.security.AuthenticationService
import ru.mint.rest.security.RefreshTokenCookieFactory

fun Route.signIn() {

    val authenticationService by closestDI().instance<AuthenticationService>()
    val refreshTokenCookieFactory by closestDI().instance<RefreshTokenCookieFactory>()

    post("sign-in") {
        val request = call.receive<SignInRequest>()
        when (val result = authenticationService.authenticate(request)) {
            is Authenticated -> call.sendTokens(result.tokens, refreshTokenCookieFactory)
            else -> call.respond(HttpStatusCode.Unauthorized)
        }
    }
}