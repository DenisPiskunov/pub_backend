package ru.mint.plugins

import com.auth0.jwt.algorithms.Algorithm

data class JWTOptions(val secret: String = "b2ed3290388e4584a64f209c030a648b",
                      val issuer: String = "ru.mint",
                      val validityInMs: Int = 15 * 60 * 1000,
                      val algorithm: Algorithm = Algorithm.HMAC512(secret))
