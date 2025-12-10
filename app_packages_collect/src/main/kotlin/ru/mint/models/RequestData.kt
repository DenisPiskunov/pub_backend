package ru.mint.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestData(
    val headers: List<String>?,
    val cookies: List<String>?,
    val remoteAddress: String?,
    val remoteHost: String?,
    val remotePort: Int?,
    val localHost: String?,
    val serverHost: String?,
    val localPort : Int?,
    val serverPort : Int?,
    val localAddress: String?,
    val uri: String?,
    val version : String?
)