package ru.mint.rest.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/block/{uuid}")
data class MasterAccountBlock(val uuid: String)
