package ru.mint.service

import ru.mint.service.dto.RefreshTokenDTO
import java.util.*

class RefreshTokenFactoryImpl(private val tokenTtlInSec: Int) : RefreshTokenFactory {

    override fun newToken() = RefreshTokenDTO(UUID.randomUUID().toString(), tokenTtlInSec)
}