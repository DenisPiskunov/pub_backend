package ru.mint.rest.security

import io.ktor.http.*
import ru.mint.service.dto.RefreshTokenDTO

interface RefreshTokenCookieFactory {

    fun new(refreshToken: RefreshTokenDTO, cookiePath: String): Cookie
}