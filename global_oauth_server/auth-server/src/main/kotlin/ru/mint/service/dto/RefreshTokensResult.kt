package ru.mint.service.dto

sealed class RefreshTokensResult {

    data class TokensRefreshed(val tokens: AccountTokensDTO) : RefreshTokensResult()

    object TokenNotFound : RefreshTokensResult()

    object TokenExpired : RefreshTokensResult()

    data class InvalidAccountStatus(val accountStatusCheckResult: AccountStatusCheckResult) : RefreshTokensResult()
}