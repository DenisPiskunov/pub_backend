package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.config.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.version(config: ApplicationConfig) {
    
       get("version") {
           call.respond(config.property("application.version").getString())
       }

}