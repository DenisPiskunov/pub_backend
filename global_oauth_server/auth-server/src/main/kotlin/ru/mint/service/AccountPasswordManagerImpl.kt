package ru.mint.service

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import ru.mint.dao.AccountEntity
import ru.mint.dao.AccountResetPasswordSecretCodeEntity
import ru.mint.dao.AccountResetPasswordSecretCodeTable
import ru.mint.dao.AccountTable
import ru.mint.service.dto.*
import ru.mint.service.dto.ForgotPasswordResult.*
import ru.mint.service.dto.ResetPasswordResult.*
import ru.mint.service.mail.MailService
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

// TODO: create data class with options for reset password (resetPswdCodeLength, resetPswdCodeTtl etc)
class AccountPasswordManagerImpl(
    private val accountStatusChecker: AccountStatusChecker,
    private val randomStringGenerator: RandomStringGenerator,
    private val resetPswdCodeLength: Int,
    private val resetPswdCodeTtl: Long,
    private val resetPswdUrlTemplate: String,
    private val mailService: MailService,
    private val passwordEncoder: PasswordEncoder,
    private val accountResetPasswordSecretCodeChecker: AccountResetPasswordSecretCodeChecker,
) : AccountPasswordManager {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun forgotPassword(email: String): ForgotPasswordResult {
        val account = transaction { AccountEntity.find { AccountTable.email eq email }.firstOrNull() } ?: return AccountNotFound
        val checkResult = accountStatusChecker.check(account)
        if (AccountStatusCheckResult.Ok != checkResult) {
            logger.info("Account with specified email $email has invalid status.")
            return InvalidAccountStatus(checkResult)
        }
        val resetPwdSecretCode = generateResetPwdSecretCode()
        transaction {
            AccountResetPasswordSecretCodeTable.deleteWhere { AccountResetPasswordSecretCodeTable.account eq account.id }
            AccountResetPasswordSecretCodeEntity.new {
                code = resetPwdSecretCode
                this.account = account
                expirationDate = LocalDateTime.now().plus(resetPswdCodeTtl, ChronoUnit.MILLIS)
            }
        }
        val resetPswdURL = String.format(resetPswdUrlTemplate, resetPwdSecretCode)
        mailService.sendResetPasswordEmail(account.email, resetPswdURL)
        return ResetPasswordURLCreated
    }

    private fun generateResetPwdSecretCode(): String {
        var code: String
        do {
            code = randomStringGenerator.generate(resetPswdCodeLength)
        } while (!isResetPwdSecretCodeUnique(code))
        return code
    }

    private fun isResetPwdSecretCodeUnique(code: String): Boolean {
        return transaction {
            AccountResetPasswordSecretCodeEntity.find { AccountResetPasswordSecretCodeTable.code eq code }.firstOrNull() == null
        }
    }

    override fun resetPassword(code: String, newPswd: String, newPswdConfirm: String): ResetPasswordResult {
        if (newPswd != newPswdConfirm) return PasswordConfirmationFailed
        val checkResult = accountResetPasswordSecretCodeChecker.check(code)
        return if (checkResult is AccountResetPasswordSecretCodeCheckResult.Ok) {
            val resetPswdSecretCodeEntity = checkResult.entity
            transaction {
                resetPswdSecretCodeEntity.account.password = passwordEncoder.encode(newPswd)
                resetPswdSecretCodeEntity.delete()
            }
            PasswordWasChanged
        } else
            InvalidSecretCode(checkResult)
    }

    override fun changePassword(accountUUID: UUID, pswd: String, pswdConfirm: String): ChangePasswordResult {
        val account = transaction { AccountEntity.findById(accountUUID) } ?: return ChangePasswordResult.AccountNotFound
        val checkResult = accountStatusChecker.check(account)
        if (AccountStatusCheckResult.Ok != checkResult) {
            logger.info("Account with specified UUID $accountUUID has invalid status.")
            return ChangePasswordResult.InvalidAccountStatus(checkResult)
        }
        if (pswd != pswdConfirm) {
            logger.info("Password confirmation failed for account with UUID $accountUUID.")
            return ChangePasswordResult.PasswordConfirmationFailed
        }
        transaction {
            account.password = passwordEncoder.encode(pswd)
        }
        return ChangePasswordResult.PasswordWasChanged

    }

}