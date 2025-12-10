package ru.mint.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object AccountResetPasswordSecretCodeTable : LongIdTable("account_reset_password_secret_code") {

    val code = char("code", 15).uniqueIndex("uq_account_reset_password_secret_code_code")
    val account = reference("account_uuid", AccountTable, fkName = "fk_account_reset_password_secret_code_account_id")
    val expirationDate = datetime("expiration_date")

}

class AccountResetPasswordSecretCodeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountResetPasswordSecretCodeEntity>(AccountResetPasswordSecretCodeTable)

    var code by AccountResetPasswordSecretCodeTable.code
    var account by AccountEntity referencedOn AccountResetPasswordSecretCodeTable.account
    var expirationDate by AccountResetPasswordSecretCodeTable.expirationDate
}