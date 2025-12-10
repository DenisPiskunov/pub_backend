package ru.mint.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import java.util.*
import javax.sql.DataSource

private fun newDataSource(): DataSource = HikariDataSource(HikariConfig(
    Properties()
        .apply {
            setProperty("dataSourceClassName", dataSourceClassName)
            setProperty("dataSource.user", dbUser)
            setProperty("dataSource.password", dbPassword)
            setProperty("dataSource.databaseName", databaseName)
            setProperty("dataSource.portNumber", dbPortNumber)
            setProperty("dataSource.serverName", dbServerName)
        }
))

fun configureDatabases() : Database {
    return Database.connect(newDataSource())
}
