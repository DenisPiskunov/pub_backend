package ru.mint.service

import ru.mint.dao.AuthorityEntity
import ru.mint.service.converter.AuthorityConverter
import ru.mint.service.dto.AuthorityDTO
import org.jetbrains.exposed.sql.transactions.transaction

class AuthorityServiceImpl(private val authorityConverter: AuthorityConverter) : AuthorityService {

    override fun findAll(): List<AuthorityDTO> {
        return transaction {
            AuthorityEntity.all().map {
                authorityConverter.toDTO(it)
            }
        }
    }
}