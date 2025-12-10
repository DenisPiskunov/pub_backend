package ru.mint.rest.model

import kotlinx.serialization.Serializable

@Serializable
class AccessTokenResponse(val accessToken: String)