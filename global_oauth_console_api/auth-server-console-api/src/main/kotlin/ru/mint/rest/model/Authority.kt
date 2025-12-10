package ru.mint.rest.model

import kotlinx.serialization.Serializable
import ru.mint.dao.Authorities
import ru.mint.service.dto.AuthorityDTO

@Serializable
data class Authority(val id: Long, val name: Authorities) {

    companion object {

        fun new(dto: AuthorityDTO) = Authority(dto.id, dto.name)
    }
}