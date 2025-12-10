package ru.mint.service.dto

import java.util.*

data class AccountDTO(val uuid: UUID, val login: String, val roles: List<RoleDTO>)