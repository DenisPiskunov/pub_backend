package ru.mint.plugins

import io.ktor.application.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.configureCORS() {
    install(CORS) {
        val corsConfig = environment.config.config("application.CORS")
        configureMethods(corsConfig)
        configureHeaders(corsConfig)
        allowCredentials = corsConfig.property("allowCredentials").getString().toBoolean()
        configureHosts(corsConfig)
    }
}

private fun CORS.Configuration.configureMethods(corsConfig: ApplicationConfig) {
    corsConfig.property("methods").getList().forEach {
        method(HttpMethod.parse(it))
    }
}

private fun CORS.Configuration.configureHeaders(corsConfig: ApplicationConfig) {
    corsConfig.property("headers").getList().forEach {
        header(it)
    }
}

private fun CORS.Configuration.configureHosts(corsConfig: ApplicationConfig) {
    corsConfig.property("hosts").getList().forEach {
        hosts.add(it)
    }
}