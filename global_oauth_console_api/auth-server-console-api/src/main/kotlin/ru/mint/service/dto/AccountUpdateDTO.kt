package ru.mint.service.dto

import java.util.*

data class AccountUpdateDTO(val uuid: UUID, val rolesIds: List<Long>)