package ru.mint.service

import ru.mint.service.dto.RefreshTokenDTO

interface RefreshTokenFactory {

    fun newToken() : RefreshTokenDTO
}