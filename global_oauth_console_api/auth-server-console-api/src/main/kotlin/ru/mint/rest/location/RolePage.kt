package ru.mint.rest.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("")
data class RolePage(val limit: Int, val offset: Int)
