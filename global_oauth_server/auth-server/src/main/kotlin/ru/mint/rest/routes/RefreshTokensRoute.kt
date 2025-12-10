package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.mint.rest.security.RefreshTokenCookieFactory
import ru.mint.service.AccountTokensIssuer
import ru.mint.service.dto.RefreshTokensResult.TokensRefreshed

fun Route.refreshTokens() {

    val refreshTokenCookieFactory by closestDI().instance<RefreshTokenCookieFactory>()
    val accountTokensIssuer by closestDI().instance<AccountTokensIssuer>()

    post("refresh-tokens") {
        val refreshTokenCookie = call.request.cookies["refreshToken"]
        if (refreshTokenCookie.isNullOrBlank()) {
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            when (val result = accountTokensIssuer.refreshTokens(refreshTokenCookie)) {
                is TokensRefreshed -> call.sendTokens(result.tokens, refreshTokenCookieFactory)
                else -> call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}