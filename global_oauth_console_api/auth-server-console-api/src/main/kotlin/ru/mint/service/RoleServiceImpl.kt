package ru.mint.service

import ru.mint.dao.AuthorityEntity
import ru.mint.dao.RoleEntity
import ru.mint.dao.RoleTable
import ru.mint.service.converter.RoleConverter
import ru.mint.service.dto.PageDTO
import ru.mint.service.dto.RoleCreateDTO
import ru.mint.service.dto.RoleDTO
import ru.mint.service.dto.RoleUpdateDTO
import org.jetbrains.exposed.sql.transactions.transaction

class RoleServiceImpl(private val roleConverter: RoleConverter) : RoleService {

    override fun findById(id: Long): RoleDTO {
        return transaction {
            roleConverter.toDTO(RoleEntity.findById(id)!!)
        }
    }

    override fun findNotDeleted(): List<RoleDTO> {
        return transaction {
            RoleEntity.find { RoleTable.isDeleted eq false }
                .map { roleConverter.toDTO(it) }
        }
    }

    override fun page(limit: Int, offset: Int): PageDTO<RoleDTO> {
        val count = transaction {
            RoleEntity.all().count()
        }
        val items = transaction {
            RoleEntity.all().limit(limit, offset.toLong()).map {
                roleConverter.toDTO(it)
            }
        }
        return PageDTO(items, count)
    }

    override fun create(dto: RoleCreateDTO) {
        transaction {
            val role = transaction {
                RoleEntity.new {
                    this.name = dto.name
                    isDeleted = false
                }
            }
            transaction {
                role.authorities = AuthorityEntity.forIds(dto.authoritiesIds)
            }
        }
    }

    // TODO: check if roles isDeleted  = true
    override fun update(dto: RoleUpdateDTO) {
        val role = transaction {
            RoleEntity.findById(dto.id)!!
        }
        transaction {
            role.name = dto.name
            role.authorities = AuthorityEntity.forIds(dto.authoritiesIds)
        }
    }

    override fun markAsDeleted(id: Long) {
        val role = transaction {
            RoleEntity.findById(id)
        }
//         TODO: remove this role from all accounts
        if (role != null && !role.isDeleted) {
            transaction {
                role.isDeleted = true
            }
        }
    }
}