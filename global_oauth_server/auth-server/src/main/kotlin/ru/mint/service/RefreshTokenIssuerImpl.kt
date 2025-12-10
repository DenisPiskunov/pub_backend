package ru.mint.service

import org.jetbrains.exposed.sql.transactions.transaction
import ru.mint.dao.AccountEntity
import ru.mint.dao.RefreshTokenEntity
import ru.mint.service.dto.RefreshTokenDTO
import java.time.LocalDateTime

class RefreshTokenIssuerImpl(private val refreshTokenEncoder: RefreshTokenEncoder, private val refreshTokenFactory: RefreshTokenFactory) :
    RefreshTokenIssuer {

    override fun issueToken(account: AccountEntity): RefreshTokenDTO {
        val refreshToken = refreshTokenFactory.newToken()
        transaction {
            RefreshTokenEntity.new {
                token = refreshTokenEncoder.encode(refreshToken.token)
                this.account = account
                expirationDate = LocalDateTime.now().plusSeconds(refreshToken.ttlInSec.toLong())
            }
        }
        return refreshToken
    }
}