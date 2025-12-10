package ru.mint.rest.model.role

import kotlinx.serialization.Serializable
import ru.mint.service.dto.RoleUpdateDTO

@Serializable
data class RoleUpdate(val name: String, val authoritiesIds: List<Long>) {

    fun toDTO(roleId: Long) = RoleUpdateDTO(roleId, name, authoritiesIds)
}