package ru.mint.service

import ru.mint.service.dto.AccountResetPasswordSecretCodeCheckResult

interface AccountResetPasswordSecretCodeChecker {

    fun check(code: String): AccountResetPasswordSecretCodeCheckResult

}