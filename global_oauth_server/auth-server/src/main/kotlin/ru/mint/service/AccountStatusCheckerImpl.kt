package ru.mint.service

import ru.mint.dao.AccountEntity
import ru.mint.service.dto.AccountStatusCheckResult
import ru.mint.service.dto.AccountStatusCheckResult.*

class AccountStatusCheckerImpl : AccountStatusChecker {

    override fun check(account: AccountEntity): AccountStatusCheckResult {
        return when {
            account.isBlocked -> AccountIsBlocked
            account.isDeleted -> AccountIsDeleted
            else -> Ok
        }
    }
}