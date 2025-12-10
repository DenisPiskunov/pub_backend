package ru.mint.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object AuthorityTable : LongIdTable("authority") {
    val name = enumerationByName("name", 40, Authorities::class).uniqueIndex("uq_authority_name")
}

class AuthorityEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AuthorityEntity>(AuthorityTable)

    var name by AuthorityTable.name

}