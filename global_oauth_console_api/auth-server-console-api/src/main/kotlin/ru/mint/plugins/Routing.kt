package ru.mint.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.routing.*
import ru.mint.rest.routes.*

@KtorExperimentalLocationsAPI
fun Application.configureRouting() {

    routing {
        route("/api-console/") {
            authenticate {
                masterAccounts()
                accounts()
                roles()
                authorities()
            }
            version(application.environment.config)
        }
    }
}
