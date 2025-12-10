package ru.mint.service

import ru.mint.service.dto.*
import java.util.*

interface MasterAccountService {

    fun findValid() : List<MasterAccountDTO>

    fun findById(uuid: UUID): MasterAccountDTO

    fun page(limit: Int, offset: Int): PageDTO<MasterAccountDTO>

    fun create(dto: MasterAccountCreateDTO)

    fun update(dto: MasterAccountUpdateDTO)

    fun markAsDeleted(uuid: UUID)

    fun block(uuid: UUID)
}