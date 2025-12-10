package ru.mint.rest.model.account.master

import kotlinx.serialization.Serializable
import ru.mint.service.dto.MasterAccountUpdateDTO
import java.util.*

@Serializable
data class MasterAccountUpdate(val login: String, val email: String) {

    fun toDTO(uuid: String) = MasterAccountUpdateDTO(UUID.fromString(uuid), login, email)
}
