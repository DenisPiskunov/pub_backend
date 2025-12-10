package ru.mint.service.dto

data class RefreshTokenDTO(val token: String, val ttlInSec: Int)