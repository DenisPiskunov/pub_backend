package ru.mint.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.resources.Resources
import io.ktor.server.application.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.Database
import ru.mint.converters.RequestConverter

fun Application.configureRouting(database: Database) {
    val payloadService = PayloadService(database)
    install(Resources)

    routing {
        post("api/save-payload") {
            val payloadsData = call.receive<PayloadsData>()
            val requestData = RequestConverter(call.request).getRequestData()
            val id = payloadService.create(database, payloadsData, requestData)
            call.respond(HttpStatusCode.Created, id)
        }

        get("api/ping") {
            call.respondText("Ping reply")
        }
    }
}

