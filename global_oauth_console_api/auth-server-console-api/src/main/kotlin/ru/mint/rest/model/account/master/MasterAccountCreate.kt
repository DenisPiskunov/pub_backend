package ru.mint.rest.model.account.master

import kotlinx.serialization.Serializable
import ru.mint.service.dto.MasterAccountCreateDTO

@Serializable
data class MasterAccountCreate(val login: String, val email: String) {

    fun toDTO() = MasterAccountCreateDTO(login, email)
}
