package ru.mint.rest.model.role

import kotlinx.serialization.Serializable
import ru.mint.service.dto.RoleDTO

@Serializable
class RolePageItem(val id: Long, val name: String, val isDeleted: Boolean) {

    companion object {

        fun new(dto: RoleDTO) = RolePageItem(dto.id, dto.name, dto.isDeleted)
    }
}