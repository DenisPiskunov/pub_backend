package ru.mint.service

import com.auth0.jwt.JWT
import java.util.*

class JWTFactory(private val options: JWTOptions) : AccessTokenFactory {

    private val authSubject = "Authentication"
    private val uuidClaim = "uuid"

    override fun newToken(accountUUID: UUID): String {
        return JWT.create()
            .withSubject(authSubject)
            .withIssuer(options.issuer)
            .withClaim(uuidClaim, accountUUID.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + options.validityInMs))
            .sign(options.algorithm)
    }
}