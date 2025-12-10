package ru.mint.rest.model

import kotlinx.serialization.Serializable

@Serializable
class SignOutRequest(val session: String)