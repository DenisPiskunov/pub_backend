package ru.mint.rest.model.role

import kotlinx.serialization.Serializable
import ru.mint.rest.model.Authority
import ru.mint.service.dto.RoleDTO

@Serializable
data class Role(val id: Long, val name: String, val authoritiesIds: List<Long>) {

    companion object {

        fun new(dto: RoleDTO): Role {
            return Role(dto.id, dto.name, dto.authorities.map { it.id })
        }
    }
}

