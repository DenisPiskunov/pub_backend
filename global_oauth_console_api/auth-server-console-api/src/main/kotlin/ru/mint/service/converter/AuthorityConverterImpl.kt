package ru.mint.service.converter

import ru.mint.dao.AuthorityEntity
import ru.mint.service.dto.AuthorityDTO

class AuthorityConverterImpl : AuthorityConverter {

    override fun toDTO(entity: AuthorityEntity) = AuthorityDTO(entity.id.value, entity.name)
}