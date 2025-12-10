package ru.mint.rest.security

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import ru.mint.dao.RefreshTokenTable
import ru.mint.rest.security.SignOutService.Session
import ru.mint.rest.security.SignOutService.Session.ALL
import ru.mint.rest.security.SignOutService.Session.CURRENT
import ru.mint.service.RefreshTokenEncoder
import java.util.*

class SignOutServiceImpl(private val refreshTokenEncoder: RefreshTokenEncoder) : SignOutService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun signOut(accountUUID: UUID, refreshToken: String, session: Session) {
        when (session) {
            CURRENT -> {
                logger.info("Sign out of current session for account with id = $accountUUID")
                val encodedToken = refreshTokenEncoder.encode(refreshToken)
                transaction {
                    RefreshTokenTable.deleteWhere { RefreshTokenTable.account eq accountUUID and (RefreshTokenTable.token eq encodedToken) }
                }
            }
            ALL -> {
                logger.info("Sign out of all sessions for account with id = $accountUUID")
                transaction {
                    RefreshTokenTable.deleteWhere { RefreshTokenTable.account eq accountUUID }
                }
            }
        }
    }
}