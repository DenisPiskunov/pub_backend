package ru.mint.service

import org.slf4j.LoggerFactory
import java.util.*

class AccessTokenIssuerImpl(private val accessTokenFactory: AccessTokenFactory) : AccessTokenIssuer {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun issueToken(accountId: UUID): String {
        logger.info("Issue access token for account with id = $accountId")
        return accessTokenFactory.newToken(accountId)
    }
}