package ru.mint.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.routing.*
import ru.mint.rest.routes.*


fun Application.configureRouting() {

    routing {

        route("/api/") {
            signIn()
            refreshTokens()
            authenticate {
                signOut()
            }
            accountPassword()
            version(application.environment.config)
        }

        static("/assets") {
            resources("/static")
        }
    }
}
