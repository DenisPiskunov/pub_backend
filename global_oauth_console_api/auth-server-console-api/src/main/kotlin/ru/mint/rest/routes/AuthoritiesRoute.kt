package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.mint.dao.Authorities.ROLE_CREATE
import ru.mint.plugins.hasAuthority
import ru.mint.rest.model.Authority
import ru.mint.service.AuthorityService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.authorities() {

    val authorityService by closestDI().instance<AuthorityService>()

    route("authorities") {

        hasAuthority(ROLE_CREATE) {
            get {
                call.respond(authorityService.findAll().map { Authority.new(it) })
            }
        }

    }
}