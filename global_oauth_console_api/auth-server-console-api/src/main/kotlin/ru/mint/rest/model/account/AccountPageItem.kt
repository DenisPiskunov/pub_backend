package ru.mint.rest.model.account

import kotlinx.serialization.Serializable
import ru.mint.rest.serialization.UUIDSerializer
import ru.mint.service.dto.AccountDTO
import java.util.*

@Serializable
data class AccountPageItem(@Serializable(with = UUIDSerializer::class) val uuid: UUID, val login: String) {

    companion object {

        fun new(dto: AccountDTO) = AccountPageItem(dto.uuid, dto.login)
    }
}