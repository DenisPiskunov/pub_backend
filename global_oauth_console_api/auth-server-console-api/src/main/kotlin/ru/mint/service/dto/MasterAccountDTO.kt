package ru.mint.service.dto

import java.time.LocalDateTime
import java.util.*

data class MasterAccountDTO(
    val uuid: UUID,
    val login: String,
    val email: String,
    val isBlocked: Boolean,
    val isDeleted: Boolean,
    val creationDate: LocalDateTime
)