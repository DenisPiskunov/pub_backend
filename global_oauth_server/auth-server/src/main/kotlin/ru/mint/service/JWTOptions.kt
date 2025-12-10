package ru.mint.service

import com.auth0.jwt.algorithms.Algorithm

data class JWTOptions(val secret: String, val issuer: String, val validityInMs: Int, val algorithm: Algorithm = Algorithm.HMAC512(secret))