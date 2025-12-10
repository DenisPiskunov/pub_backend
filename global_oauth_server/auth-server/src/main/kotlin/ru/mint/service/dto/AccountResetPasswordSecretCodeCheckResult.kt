package ru.mint.service.dto

import ru.mint.dao.AccountResetPasswordSecretCodeEntity

sealed class AccountResetPasswordSecretCodeCheckResult {

    class Ok(val entity: AccountResetPasswordSecretCodeEntity) : AccountResetPasswordSecretCodeCheckResult()

    object SecretCodeWasNotFound : AccountResetPasswordSecretCodeCheckResult()

    object SecretCodeLifeTimeExpired : AccountResetPasswordSecretCodeCheckResult()
}
