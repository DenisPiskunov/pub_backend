package ru.mint.rest.security

import io.ktor.http.*
import ru.mint.service.dto.RefreshTokenDTO

class RefreshTokenCookieFactoryImpl(private val isHttpOnly: Boolean, private val cookieName: String) : RefreshTokenCookieFactory {

    override fun new(refreshToken: RefreshTokenDTO, cookiePath: String): Cookie {
        return Cookie(
            name = cookieName,
            value = refreshToken.token,
            maxAge = refreshToken.ttlInSec,
            httpOnly = true,
            path = cookiePath,
            secure = isHttpOnly,
            extensions = mapOf("SameSite" to "None")
        )
    }
}