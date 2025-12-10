package ru.mint.rest.model

import kotlinx.serialization.Serializable

@Serializable
class SignInRequest(val login: String, val password: String)