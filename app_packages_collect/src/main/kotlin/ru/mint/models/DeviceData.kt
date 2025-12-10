package ru.mint.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceData(
    val deviceModel: String?,
    val deviceName: String?,
    val deviceType: String?,
    val deviceUniqueIdentifier: String?,
    val graphicsDeviceName: String?,
    val graphicsDeviceVendor: String?,
    val graphicsMemorySize: Int?,
    val operatingSystem: String?,
    val processorType: String?,
    val processorCount: Int?,
    val processorFrequency: Int?,
    val systemMemorySize: Int?,
    val supportsAccelerometer: Boolean?,
    val supportsGyroscope: Boolean?,
    val supportsAudio: Boolean?,
    val supportsLocationService: Boolean?,
    val supportsVibration: Boolean,
    val batteryLevel : Float,
    val batteryStatus: String,
    val brand: String?,
    val device: String?,
    val isSimAvailable: Boolean,
    val manufacture: String?,
    val board: String?,
    val bootloader: String?,
    val id: String?,
    val display: String?,
    val fingerprint: String?,
    val hardware: String?,
    val host: String?,
    val product: String?,
    val radioVersion: String?,
    val tags: String?,
    val user: String?,
    val installedData: List<InstalledData>?
)