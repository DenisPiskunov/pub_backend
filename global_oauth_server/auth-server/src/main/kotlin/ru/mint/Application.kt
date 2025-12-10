package ru.mint

import io.ktor.application.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.mint.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

/**
 * Please note that you can use any other name instead of *module*.
 * Also note that you can have more then one modules in your application.
 * */
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    configureAdministration()

    configureSerialization()

    install(XForwardedHeaderSupport)

    configureCORS()

    configureAuthentication()

    configureDatabase()

    configureDI()

    configureRouting()

}
