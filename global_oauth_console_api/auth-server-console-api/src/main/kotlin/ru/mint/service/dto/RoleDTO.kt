package ru.mint.service.dto

import java.time.LocalDateTime

data class RoleDTO(val id: Long, val name: String, val isDeleted: Boolean, val creationDate: LocalDateTime, val authorities: List<AuthorityDTO>)