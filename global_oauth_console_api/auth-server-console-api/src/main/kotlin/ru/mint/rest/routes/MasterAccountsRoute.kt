package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import ru.mint.dao.Authorities.*
import io.ktor.routing.*
import ru.mint.plugins.hasAuthority
import ru.mint.rest.location.ByUUID
import ru.mint.rest.location.MasterAccountBlock
import ru.mint.rest.location.MasterAccountPage
import ru.mint.rest.model.Page
import ru.mint.rest.model.account.master.*
import ru.mint.service.MasterAccountService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.*

@KtorExperimentalLocationsAPI
fun Route.masterAccounts() {

    val masterAccountService by closestDI().instance<MasterAccountService>()

    route("m-accounts") {

        hasAuthority(MASTER_ACCOUNT_READ) {
            get("/enabled") {
                val accounts = masterAccountService.findValid().map { MasterAccountListItem.new(it) }
                call.respond(accounts)
            }
        }

        hasAuthority(MASTER_ACCOUNT_READ) {
            get<ByUUID> { location ->
                val dto = masterAccountService.findById(UUID.fromString(location.uuid))
                call.respond(HttpStatusCode.OK, MasterAccount.new(dto))
            }
        }

        hasAuthority(MASTER_ACCOUNT_READ) {
            get<MasterAccountPage> { location ->
                val (items, totalCount) = masterAccountService.page(location.limit, location.offset)
                val page = Page(items.map { MasterAccountPageItem.new(it) }, totalCount)
                call.respond(page)
            }
        }

        hasAuthority(MASTER_ACCOUNT_CREATE) {
            post {
                val request = call.receive<MasterAccountCreate>()
                masterAccountService.create(request.toDTO())
                call.respond(HttpStatusCode.Created)
            }
        }

        hasAuthority(MASTER_ACCOUNT_UPDATE) {
            put<ByUUID> { location ->
                val request = call.receive<MasterAccountUpdate>()
                masterAccountService.update(request.toDTO(location.uuid))
                call.respond(HttpStatusCode.OK)
            }
        }

        hasAuthority(MASTER_ACCOUNT_DELETE) {
            delete<ByUUID> { location ->
                masterAccountService.markAsDeleted(UUID.fromString(location.uuid))
                call.respond(HttpStatusCode.OK)
            }
        }

        hasAuthority(MASTER_ACCOUNT_BLOCK) {
            put<MasterAccountBlock> { location ->
                masterAccountService.block(UUID.fromString(location.uuid))
                call.respond(HttpStatusCode.OK)
            }
        }

    }

}