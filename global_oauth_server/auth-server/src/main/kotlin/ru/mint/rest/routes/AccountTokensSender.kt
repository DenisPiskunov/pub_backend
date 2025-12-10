package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.response.*
import ru.mint.rest.model.AccessTokenResponse
import ru.mint.rest.security.RefreshTokenCookieFactory
import ru.mint.service.dto.AccountTokensDTO

suspend inline fun ApplicationCall.sendTokens(tokens: AccountTokensDTO, refreshTokenCookieFactory: RefreshTokenCookieFactory) {
    val refreshToken = tokens.refreshToken
    val refreshTokenCookie = refreshTokenCookieFactory.new(refreshToken, "/")
    response.cookies.append(refreshTokenCookie)
    respond(AccessTokenResponse(tokens.accessToken))
}