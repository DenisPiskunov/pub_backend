package ru.mint.rest.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("")
data class MasterAccountPage(val limit: Int, val offset: Int)
