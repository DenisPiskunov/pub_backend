package ru.mint.rest.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("")
data class AccountPage(val limit: Int, val offset: Int)
