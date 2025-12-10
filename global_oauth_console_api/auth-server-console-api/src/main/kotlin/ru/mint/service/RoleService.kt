package ru.mint.service

import ru.mint.service.dto.*

interface RoleService {

    fun findById(id: Long): RoleDTO

    fun findNotDeleted(): List<RoleDTO>

    fun page(limit: Int, offset: Int): PageDTO<RoleDTO>

    fun create(dto: RoleCreateDTO)

    fun update(dto: RoleUpdateDTO)

    fun markAsDeleted(id: Long)
}