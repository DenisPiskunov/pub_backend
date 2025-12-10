package ru.mint.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object RoleTable : LongIdTable("role") {
    val name = varchar("name", 50).uniqueIndex("uq_role_name")
    val isDeleted = bool("is_deleted").default(false)
    val creationDate = datetime("creation_date").default(LocalDateTime.now())
}

class RoleEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleEntity>(RoleTable)

    var name by RoleTable.name
    var isDeleted by RoleTable.isDeleted
    val creationDate by RoleTable.creationDate
    var authorities by AuthorityEntity via RoleAuthorityTable
}