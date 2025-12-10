package ru.mint.service.dto

sealed class ResetPasswordResult {

    object PasswordWasChanged : ResetPasswordResult()

    object PasswordConfirmationFailed : ResetPasswordResult()

    class InvalidSecretCode(val result: AccountResetPasswordSecretCodeCheckResult) : ResetPasswordResult()

}
