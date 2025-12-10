package ru.mint.service

import ru.mint.service.dto.AuthorityDTO

interface AuthorityService {

    fun findAll(): List<AuthorityDTO>
}