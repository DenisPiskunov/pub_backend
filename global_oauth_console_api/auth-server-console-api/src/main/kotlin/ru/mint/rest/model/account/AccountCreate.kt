package ru.mint.rest.model.account

import kotlinx.serialization.Serializable
import ru.mint.rest.serialization.UUIDSerializer
import ru.mint.service.dto.AccountCreateDTO
import java.util.*

@Serializable
data class AccountCreate(@Serializable(with = UUIDSerializer::class) val uuid: UUID, val rolesIds: List<Long>) {

    fun toDTO() = AccountCreateDTO(uuid, rolesIds)
}