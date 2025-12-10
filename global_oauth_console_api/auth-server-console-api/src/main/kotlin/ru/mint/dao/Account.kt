package ru.mint.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object AccountTable : UUIDTable("account", "uuid") {

    val creationDate = datetime("creation_date").default(LocalDateTime.now())
}

class AccountEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccountEntity>(AccountTable)

    var creationDate by AccountTable.creationDate
    var roles by RoleEntity via AccountRoleTable

}