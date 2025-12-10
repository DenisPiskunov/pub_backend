package ru.mint.service

import org.jetbrains.exposed.sql.transactions.transaction
import ru.mint.dao.AccountResetPasswordSecretCodeEntity
import ru.mint.dao.AccountResetPasswordSecretCodeTable
import ru.mint.service.dto.AccountResetPasswordSecretCodeCheckResult
import ru.mint.service.dto.AccountResetPasswordSecretCodeCheckResult.*
import java.time.LocalDateTime

class AccountResetPasswordSecretCodeCheckerImpl : AccountResetPasswordSecretCodeChecker {

    override fun check(code: String): AccountResetPasswordSecretCodeCheckResult {
        val resetPswdSecretCodeEntity = transaction {
            AccountResetPasswordSecretCodeEntity.find { AccountResetPasswordSecretCodeTable.code eq code }.firstOrNull()
        }
            ?: return SecretCodeWasNotFound
        if (LocalDateTime.now() > resetPswdSecretCodeEntity.expirationDate) {
            return SecretCodeLifeTimeExpired
        }
        return Ok(resetPswdSecretCodeEntity)
    }
}