package ru.mint.rest.model.account.master

import kotlinx.serialization.Serializable
import ru.mint.rest.serialization.UUIDSerializer
import ru.mint.service.dto.MasterAccountDTO
import java.util.*

@Serializable
data class MasterAccountPageItem(
    @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    val login: String,
    val email: String,
    val isBlocked: Boolean,
    val isDeleted: Boolean
) {
    companion object {

        fun new(dto: MasterAccountDTO) = MasterAccountPageItem(dto.uuid, dto.login, dto.email, dto.isBlocked, dto.isDeleted)
    }

}