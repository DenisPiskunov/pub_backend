package ru.mint.dao

import org.jetbrains.exposed.sql.Table

object RoleAuthorityTable: Table("role_to_authority") {
    val role = reference("role_id", RoleTable)
    val authority = reference("authority_id", AuthorityTable)

    override val primaryKey = PrimaryKey(role, authority, name = "pk_role_to_authority")
}