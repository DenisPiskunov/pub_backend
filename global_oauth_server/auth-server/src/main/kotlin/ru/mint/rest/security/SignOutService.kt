package ru.mint.rest.security

import java.util.*

interface SignOutService {

    fun signOut(accountUUID: UUID, refreshToken: String, session:Session)

    enum class Session { CURRENT, ALL }
}