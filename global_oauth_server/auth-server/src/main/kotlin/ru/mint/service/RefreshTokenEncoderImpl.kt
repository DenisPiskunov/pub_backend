package ru.mint.service

import org.springframework.security.crypto.bcrypt.BCrypt


class RefreshTokenEncoderImpl : RefreshTokenEncoder {

//    TODO: move to configs
    private val salt = "\$2a\$10\$Y3eydahPvKzt1PrCKYc33u"

    override fun encode(rawToken: String): String {
        return  BCrypt.hashpw(rawToken, salt)
    }
}