package ru.mint.rest.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/{uuid}")
data class ByUUID(val uuid: String)
