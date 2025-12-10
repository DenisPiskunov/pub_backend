package ru.mint.rest.model

import kotlinx.serialization.Serializable

@Serializable
class ChangePasswordRequest(val password: String, val passwordConfirm: String)