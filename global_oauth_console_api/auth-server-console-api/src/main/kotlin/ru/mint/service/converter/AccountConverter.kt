package ru.mint.service.converter

import ru.mint.dao.AccountEntity
import ru.mint.service.dto.AccountDTO

interface AccountConverter {

    fun toDTO(entity: AccountEntity, masterAccountLogin: String): AccountDTO
}