package ru.mint.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.config.*
import org.jetbrains.exposed.sql.Database
import java.util.*
import javax.sql.DataSource

fun Application.configureDatabase() {
    val dsProps = newDataSourceProps(environment.config.config("application.datasource"))
    val ds = newDataSource(dsProps)
    Database.connect(ds)
}

private fun newDataSource(properties: Properties): DataSource = HikariDataSource(HikariConfig(properties))

private fun newDataSourceProps(dbConfig: ApplicationConfig): Properties {
    return Properties().apply {
        setProperty("dataSourceClassName", dbConfig.property("dataSourceClassName").getString())
        setProperty("dataSource.user", dbConfig.property("user").getString())
        setProperty("dataSource.password", dbConfig.property("password").getString())
        setProperty("dataSource.databaseName", dbConfig.property("databaseName").getString())
        setProperty("dataSource.portNumber", dbConfig.property("portNumber").getString())
        setProperty("dataSource.serverName", dbConfig.property("serverName").getString())
    }
}