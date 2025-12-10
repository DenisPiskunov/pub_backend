package ru.mint.rest.security

import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import ru.mint.dao.AccountEntity
import ru.mint.dao.AccountTable
import ru.mint.rest.model.SignInRequest
import ru.mint.rest.security.AuthResult.*
import ru.mint.service.AccountStatusChecker
import ru.mint.service.AccountTokensIssuer
import ru.mint.service.PasswordEncoder
import ru.mint.service.dto.AccountStatusCheckResult.Ok

class AuthenticationServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val accountTokensIssuer: AccountTokensIssuer,
    private val accountStatusChecker: AccountStatusChecker,
) : AuthenticationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun authenticate(request: SignInRequest): AuthResult {
        val account = transaction {
            AccountEntity.find { (AccountTable.login eq request.login) or (AccountTable.email eq request.login) }.firstOrNull()
        }
        if (account == null) {
            logger.info("Account with specified login/email ${request.login} was not found.")
            return AccountNotFound
        }
        val checkResult = accountStatusChecker.check(account)
        if (Ok != checkResult) {
            logger.info("Account with specified login/email ${request.login} has invalid status.")
            return InvalidAccountStatus(checkResult)
        }
        if (!passwordEncoder.matches(request.password, account.password)) {
            logger.info("Password for account with login/email ${request.login} is invalid.")
            return InvalidPassword
        }
        return Authenticated(accountTokensIssuer.issueTokens(account))
    }
}