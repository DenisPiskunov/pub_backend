package ru.mint.rest.security

import ru.mint.rest.model.SignInRequest

interface AuthenticationService {

    fun authenticate(request: SignInRequest) : AuthResult
}