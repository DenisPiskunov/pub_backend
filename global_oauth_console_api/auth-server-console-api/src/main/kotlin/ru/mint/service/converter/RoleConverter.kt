package ru.mint.service.converter

import ru.mint.dao.RoleEntity
import ru.mint.service.dto.RoleDTO

interface RoleConverter {

    fun toDTO(entity: RoleEntity) : RoleDTO
}