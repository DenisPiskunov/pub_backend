package ru.mint.service

import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import ru.mint.dao.AccountEntity
import ru.mint.dao.RefreshTokenEntity
import ru.mint.dao.RefreshTokenTable
import ru.mint.service.dto.AccountStatusCheckResult.Ok
import ru.mint.service.dto.AccountTokensDTO
import ru.mint.service.dto.RefreshTokensResult
import ru.mint.service.dto.RefreshTokensResult.*
import java.time.LocalDateTime

class AccountTokensIssuerImpl(
    private val accessTokenIssuer: AccessTokenIssuer,
    private val refreshTokenIssuer: RefreshTokenIssuer,
    private val refreshTokenEncoder: RefreshTokenEncoder,
    private val accountStatusChecker: AccountStatusChecker
) : AccountTokensIssuer {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun issueTokens(account: AccountEntity): AccountTokensDTO {
        val accessToken = accessTokenIssuer.issueToken(account.id.value)
        val refreshToken = refreshTokenIssuer.issueToken(account)
        return AccountTokensDTO(accessToken, refreshToken)
    }

    override fun refreshTokens(refreshToken: String): RefreshTokensResult {
        val encodedToken = refreshTokenEncoder.encode(refreshToken)
        val refreshTokenEntity = transaction {
            RefreshTokenEntity.find { RefreshTokenTable.token eq encodedToken }.firstOrNull()
        }
        if (refreshTokenEntity == null) {
            logger.info("Token was not found.")
            return TokenNotFound
        }
        val checkResult = transaction {
            accountStatusChecker.check(refreshTokenEntity.account)
        }
        if (Ok != checkResult) {
            logger.info("Account with login ${refreshTokenEntity.account.login} has invalid status.")
            return InvalidAccountStatus(checkResult)
        }
        if (refreshTokenEntity.expirationDate < LocalDateTime.now()) {
            logger.info("Token expired.")
            return TokenExpired
        }
        val tokens = transaction {
            refreshTokenEntity.delete()
            issueTokens(refreshTokenEntity.account)
        }
        return TokensRefreshed(tokens)
    }
}