package ru.mint.service

interface RefreshTokenEncoder {

    fun encode(rawToken: String): String
}