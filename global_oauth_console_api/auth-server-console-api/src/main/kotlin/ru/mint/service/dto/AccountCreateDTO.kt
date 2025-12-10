package ru.mint.service.dto

import java.util.*

data class AccountCreateDTO(val uuid: UUID, val rolesIds: List<Long>)