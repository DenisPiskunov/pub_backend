package ru.mint.service.dto

sealed class AccountStatusCheckResult {

    object Ok : AccountStatusCheckResult()

    object AccountIsBlocked : AccountStatusCheckResult()

    object AccountIsDeleted : AccountStatusCheckResult()
}
