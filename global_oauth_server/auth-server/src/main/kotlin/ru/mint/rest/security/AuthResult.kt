package ru.mint.rest.security

import ru.mint.service.dto.AccountStatusCheckResult
import ru.mint.service.dto.AccountTokensDTO

sealed class AuthResult {

    data class Authenticated(val tokens: AccountTokensDTO) : AuthResult()

    object AccountNotFound : AuthResult()

    data class InvalidAccountStatus(val accountStatusCheckResult: AccountStatusCheckResult) : AuthResult()

    object InvalidPassword : AuthResult()

}