package ru.mint.service

import java.util.*

interface AccessTokenIssuer {

    fun issueToken(accountId: UUID): String
}