package ru.mint.service

import ru.mint.dao.AccountEntity
import ru.mint.service.dto.RefreshTokenDTO

interface RefreshTokenIssuer {

    fun issueToken(account: AccountEntity): RefreshTokenDTO
}