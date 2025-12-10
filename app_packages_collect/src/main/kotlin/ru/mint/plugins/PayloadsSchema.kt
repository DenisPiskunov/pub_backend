package ru.mint.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.mint.converters.UnicodeConverter
import ru.mint.models.DeviceData
import ru.mint.models.AllDataBunble
import ru.mint.models.RequestData
import java.time.LocalDateTime


@Serializable
data class PayloadsData(val payload: String)

class PayloadService(database: Database) {
    private val cryptoUtils = CryptoUtils()
    object Payload : Table() {
        val id = integer("id").autoIncrement()
        val payload = text("payload")
        val requestOriginDataTxt = text("request_origin_data")
        val creationDate = datetime("creation_date").default(LocalDateTime.now())

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Payload)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(database: Database, encryptedDeviceData: PayloadsData, requestOriginData: RequestData): Int = dbQuery {
        val decryptedPayload = cryptoUtils.decrypt(encryptedDeviceData.payload, key, iv)
        val deviceData = decodeFromString(DeviceData.serializer(), decryptedPayload)
        val payloadData = AllDataBunble(requestOriginData, deviceData)
        var payloadDataStringify = UnicodeConverter(Json.encodeToString(payloadData)).get()
        transaction(database) {
            Payload.insert {
                it[payload] = payloadDataStringify
                it[creationDate] = LocalDateTime.now()
            }[Payload.id]
        }
    }

}
