package ru.mint.service.dto

sealed class ForgotPasswordResult {

    object ResetPasswordURLCreated : ForgotPasswordResult()

    object AccountNotFound : ForgotPasswordResult()

    data class InvalidAccountStatus(val accountStatusCheckResult: AccountStatusCheckResult) : ForgotPasswordResult()
}
