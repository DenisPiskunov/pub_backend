package ru.mint.service.converter

import ru.mint.dao.AuthorityEntity
import ru.mint.service.dto.AuthorityDTO

interface AuthorityConverter {

    fun toDTO(entity: AuthorityEntity) : AuthorityDTO
}