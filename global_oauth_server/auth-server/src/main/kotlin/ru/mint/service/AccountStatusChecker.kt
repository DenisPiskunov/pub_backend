package ru.mint.service

import ru.mint.dao.AccountEntity
import ru.mint.service.dto.AccountStatusCheckResult

interface AccountStatusChecker {

    fun check(account: AccountEntity): AccountStatusCheckResult
}