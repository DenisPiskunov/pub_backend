package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.mint.dao.Authorities.*
import ru.mint.plugins.hasAuthority
import ru.mint.rest.location.ById
import ru.mint.rest.location.RolePage
import ru.mint.rest.model.Page
import ru.mint.rest.model.role.*
import ru.mint.service.RoleService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@KtorExperimentalLocationsAPI
fun Route.roles() {

    val roleService by closestDI().instance<RoleService>()

    route("roles") {

        hasAuthority(ROLE_READ) {
            get("/enabled") {
                val roles = roleService.findNotDeleted().map { RoleListItem.new(it) }
                call.respond(roles)
            }
        }

        hasAuthority(ROLE_READ) {
            get<ById> { location ->
                val dto = roleService.findById(location.id)
                call.respond(HttpStatusCode.OK, Role.new(dto))
            }
        }

        hasAuthority(ROLE_READ) {
            get<RolePage> { location ->
                val (items, totalCount) = roleService.page(location.limit, location.offset)
                val page = Page(items.map { RolePageItem.new(it) }, totalCount)
                call.respond(page)
            }
        }

        hasAuthority(ROLE_CREATE) {
            post {
                val request = call.receive<RoleCreate>()
                roleService.create(request.toDTO())
                call.respond(HttpStatusCode.Created)
            }
        }

        hasAuthority(ROLE_UPDATE) {
            put<ById> { location ->
                val request = call.receive<RoleUpdate>()
                roleService.update(request.toDTO(location.id))
                call.respond(HttpStatusCode.OK)
            }
        }

        hasAuthority(ROLE_DELETE) {
            delete<ById> { location ->
                roleService.markAsDeleted(location.id)
                call.respond(HttpStatusCode.OK)
            }
        }

    }

}