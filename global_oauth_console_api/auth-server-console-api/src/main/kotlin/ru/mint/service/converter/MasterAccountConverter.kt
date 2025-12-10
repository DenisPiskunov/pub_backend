package ru.mint.service.converter

import ru.mint.dao.MasterAccountEntity
import ru.mint.service.dto.MasterAccountDTO

interface MasterAccountConverter {

    fun toDTO(entity: MasterAccountEntity): MasterAccountDTO
}