package ru.mint.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object MasterAccountTable : UUIDTable("account", "uuid") {
    val login = varchar("login", 20).uniqueIndex("uk_account_login")
    val email = varchar("email", 30).uniqueIndex("uk_account_email")
    val password = char("password", 60)
    val isBlocked = bool("is_blocked").default(false)
    val isDeleted = bool("is_deleted").default(false)
    val creationDate = datetime("creation_date").default(LocalDateTime.now())
}

class MasterAccountEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<MasterAccountEntity>(MasterAccountTable)

    var login by MasterAccountTable.login
    var email by MasterAccountTable.email
    var password by MasterAccountTable.password
    var isBlocked by MasterAccountTable.isBlocked
    var isDeleted by MasterAccountTable.isDeleted
    var creationDate by MasterAccountTable.creationDate
}