package ru.mint.service.dto

data class RoleCreateDTO(val name: String, val authoritiesIds: List<Long>)