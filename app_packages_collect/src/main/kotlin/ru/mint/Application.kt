package ru.mint

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.forwardedheaders.*
import ru.mint.plugins.*

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun main() {
    embeddedServer(Netty, port = serverPort, host = host) {
        install(XForwardedHeaders)
        configureSerialization()
        val database = configureDatabases()
        configureAdministration()
        configureRouting(database)
    }.start(wait = true)
}