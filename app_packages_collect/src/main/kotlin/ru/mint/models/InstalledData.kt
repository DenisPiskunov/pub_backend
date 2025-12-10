package ru.mint.models

import kotlinx.serialization.Serializable

@Serializable
data class InstalledData(
    val `package`: String?,
    val name: String?
)