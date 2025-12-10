package ru.mint.service.dto

sealed class ChangePasswordResult {

    object PasswordWasChanged : ChangePasswordResult()

    object AccountNotFound : ChangePasswordResult()

    data class InvalidAccountStatus(val accountStatusCheckResult: AccountStatusCheckResult) : ChangePasswordResult()

    object PasswordConfirmationFailed : ChangePasswordResult()

}
