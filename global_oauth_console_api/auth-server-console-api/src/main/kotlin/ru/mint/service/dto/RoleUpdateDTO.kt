package ru.mint.service.dto

data class RoleUpdateDTO(val id: Long, val name: String, val authoritiesIds: List<Long>)