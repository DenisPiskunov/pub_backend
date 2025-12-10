package ru.mint.rest.model

import kotlinx.serialization.Serializable

@Serializable
class ForgotPasswordRequest(val email: String)