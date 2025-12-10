package ru.mint.service

import ru.mint.dao.AccountEntity
import ru.mint.service.dto.AccountTokensDTO
import ru.mint.service.dto.RefreshTokensResult

interface AccountTokensIssuer {

    fun issueTokens(account: AccountEntity): AccountTokensDTO

    fun refreshTokens(refreshToken: String): RefreshTokensResult
}