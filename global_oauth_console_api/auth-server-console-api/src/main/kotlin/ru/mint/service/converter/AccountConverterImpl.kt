package ru.mint.service.converter

import ru.mint.dao.AccountEntity
import ru.mint.service.dto.AccountDTO

class AccountConverterImpl(private val roleConverter: RoleConverter) : AccountConverter {

    override fun toDTO(entity: AccountEntity, masterAccountLogin: String): AccountDTO {
        return AccountDTO(entity.id.value, masterAccountLogin, entity.roles.map { roleConverter.toDTO(it) })
    }
}