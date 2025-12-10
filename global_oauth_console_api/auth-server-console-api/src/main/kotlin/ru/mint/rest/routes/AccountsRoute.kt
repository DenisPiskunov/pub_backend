package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.mint.dao.Authorities.*
import ru.mint.plugins.hasAuthority
import ru.mint.rest.location.AccountPage
import ru.mint.rest.location.ByUUID
import ru.mint.rest.model.Authority
import ru.mint.rest.model.Page
import ru.mint.rest.model.account.Account
import ru.mint.rest.model.account.AccountCreate
import ru.mint.rest.model.account.AccountPageItem
import ru.mint.rest.model.account.AccountUpdate
import ru.mint.service.AccountService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.*

@KtorExperimentalLocationsAPI
fun Route.accounts() {

    val accountService by closestDI().instance<AccountService>()

    route("accounts") {

        hasAuthority(ACCOUNT_READ) {
            get<ByUUID> { location ->
                val dto = accountService.findById(UUID.fromString(location.uuid))
                call.respond(HttpStatusCode.OK, Account.new(dto))
            }
        }

        hasAuthority(ACCOUNT_READ) {
            get<AccountPage> { location ->
                val (items, totalCount) = accountService.page(location.limit, location.offset)
                val page = Page(items.map { AccountPageItem.new(it) }, totalCount)
                call.respond(page)
            }
        }

        hasAuthority(ACCOUNT_READ) {
            get("/authorities") {
                //            TODO: refactor, create method to obtain account UUID
                val accountUUID = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("uuid").asString())
                val authorities = accountService.findAuthorities(accountUUID).map { Authority(it.id, it.name) }
                call.respond(authorities)
            }
        }

        hasAuthority(ACCOUNT_CREATE) {
            post {
                val request = call.receive<AccountCreate>()
                accountService.create(request.toDTO())
                call.respond(HttpStatusCode.Created)
            }
        }

        hasAuthority(ACCOUNT_UPDATE) {
            put {
                val request = call.receive<AccountUpdate>()
                accountService.update(request.toDTO())
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}