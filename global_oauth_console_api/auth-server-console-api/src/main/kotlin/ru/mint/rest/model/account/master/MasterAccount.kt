package ru.mint.rest.model.account.master

import kotlinx.serialization.Serializable
import ru.mint.rest.serialization.UUIDSerializer
import ru.mint.service.dto.MasterAccountDTO
import java.util.*

@Serializable
data class MasterAccount(@Serializable(with = UUIDSerializer::class) val uuid: UUID, val login: String, val email: String) {

    companion object {

        fun new(dto: MasterAccountDTO) = MasterAccount(dto.uuid, dto.login, dto.email)
    }
}
