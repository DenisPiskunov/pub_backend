package ru.mint.models

import kotlinx.serialization.Serializable

@Serializable
data class AllDataBunble(
    val requestData: RequestData,
    val deviceData: DeviceData
)
