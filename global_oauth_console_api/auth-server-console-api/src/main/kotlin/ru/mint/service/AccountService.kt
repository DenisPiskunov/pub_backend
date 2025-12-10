package ru.mint.service

import ru.mint.service.dto.*
import java.util.*

interface AccountService {

    fun findById(uuid: UUID): AccountDTO

    fun page(limit: Int, offset: Int): PageDTO<AccountDTO>

    fun findAuthorities(uuid: UUID): List<AuthorityDTO>

    fun create(dto: AccountCreateDTO)

    fun update(dto: AccountUpdateDTO)
}