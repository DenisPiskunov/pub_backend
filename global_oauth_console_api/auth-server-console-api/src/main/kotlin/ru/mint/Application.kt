package ru.mint

import io.ktor.application.*
import io.ktor.locations.*
import ru.mint.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    configureSerialization()

    install(Locations)

    configureCORS()

    configureSecurity()

    val databases = configureDatabase()

    configureDI(databases)

    configureRouting()

    configureExceptionHandling()

}