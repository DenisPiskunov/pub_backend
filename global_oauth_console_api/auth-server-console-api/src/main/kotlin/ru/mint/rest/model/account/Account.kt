package ru.mint.rest.model.account

import kotlinx.serialization.Serializable
import ru.mint.rest.serialization.UUIDSerializer
import ru.mint.service.dto.AccountDTO
import java.util.*

@Serializable
data class Account(@Serializable(with = UUIDSerializer::class) val uuid: UUID, val login: String, val rolesIds: List<Long>) {

    companion object {

        fun new(dto: AccountDTO) = Account(dto.uuid, dto.login, dto.roles.map { it.id })
    }
}