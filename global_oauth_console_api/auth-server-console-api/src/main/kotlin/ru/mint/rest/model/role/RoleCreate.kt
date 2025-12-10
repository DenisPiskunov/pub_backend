package ru.mint.rest.model.role

import kotlinx.serialization.Serializable
import ru.mint.service.dto.RoleCreateDTO

@Serializable
data class RoleCreate(val name: String, val authoritiesIds: List<Long>) {

    fun toDTO() = RoleCreateDTO(name, authoritiesIds)
}