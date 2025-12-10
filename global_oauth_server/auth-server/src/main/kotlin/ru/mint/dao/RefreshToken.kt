package ru.mint.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object RefreshTokenTable : LongIdTable("refresh_token") {

    val token = char("token", 64).uniqueIndex("uq_refresh_token_token")
    val account = reference("account_uuid", AccountTable, fkName = "fk_refresh_token_account_id")
    val expirationDate = datetime("expiration_date")
    
}

class RefreshTokenEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RefreshTokenEntity>(RefreshTokenTable)

    var token by RefreshTokenTable.token
    var account by AccountEntity referencedOn RefreshTokenTable.account
    var expirationDate by RefreshTokenTable.expirationDate
}