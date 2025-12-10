package ru.mint.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object AccountTable : UUIDTable("account", "uuid") {
    val login = varchar("login", 20).uniqueIndex("uk_account_login")
    val email = varchar("email", 20).uniqueIndex("uk_account_email")
    val password = char("password", 60)
    val isBlocked = bool("is_blocked").default(false)
    val isDeleted = bool("is_deleted").default(false)
    val creationDate = datetime("creation_date").default(LocalDateTime.now())
}

class AccountEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AccountEntity>(AccountTable)

    val login by AccountTable.login
    val email by AccountTable.email
    var password by AccountTable.password
    val isBlocked by AccountTable.isBlocked
    val isDeleted by AccountTable.isDeleted
    val creationDate by AccountTable.creationDate
}