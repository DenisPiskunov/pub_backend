package ru.mint.dao

import org.jetbrains.exposed.sql.Table

object AccountRoleTable : Table("account_to_role") {

    val account = reference("account_uuid", AccountTable)
    val role = reference("role_id", RoleTable)

    override val primaryKey = PrimaryKey(account, role, name = "pk_account_to_role")
}