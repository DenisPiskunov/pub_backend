package ru.mint.service.converter

import ru.mint.dao.MasterAccountEntity
import ru.mint.service.dto.MasterAccountDTO

class MasterAccountConverterImpl : MasterAccountConverter {

    override fun toDTO(entity: MasterAccountEntity) = MasterAccountDTO(entity.id.value, entity.login, entity.email, entity.isBlocked, entity.isDeleted, entity.creationDate)
}