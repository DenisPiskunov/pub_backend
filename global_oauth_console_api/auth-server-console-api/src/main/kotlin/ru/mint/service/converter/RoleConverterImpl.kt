package ru.mint.service.converter

import ru.mint.dao.RoleEntity
import ru.mint.service.dto.RoleDTO

class RoleConverterImpl(private val authorityConverter: AuthorityConverter) : RoleConverter {

    override fun toDTO(entity: RoleEntity): RoleDTO {
        return RoleDTO(entity.id.value, entity.name, entity.isDeleted, entity.creationDate, entity.authorities.map { authorityConverter.toDTO(it) })
    }
}