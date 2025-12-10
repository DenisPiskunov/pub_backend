package ru.mint.service

import java.util.*

interface AccessTokenFactory {

    fun newToken(accountUUID: UUID): String
}