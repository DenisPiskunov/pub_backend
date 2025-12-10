package ru.mint.rest.model.role

import kotlinx.serialization.Serializable
import ru.mint.service.dto.RoleDTO

@Serializable
data class RoleListItem(val id: Long, val name: String) {

    companion object {

        fun new(dto: RoleDTO) = RoleListItem(dto.id, dto.name)
    }
}
