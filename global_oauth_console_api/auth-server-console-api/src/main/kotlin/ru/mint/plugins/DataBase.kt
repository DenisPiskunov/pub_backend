package ru.mint.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import ru.mint.dao.Databases
import java.util.*
import javax.sql.DataSource

fun Application.configureDatabase(): Databases {
    val authServerConsoleDatabase = Database.connect(newFAuthServerConsoleDataSource(environment.config))
    val authServerDatabase = Database.connect(newAuthServerDataSource(environment.config))
    TransactionManager.defaultDatabase = authServerConsoleDatabase
    return Databases(authServerDatabase, authServerConsoleDatabase)
}

private fun newFAuthServerConsoleDataSource(appConfig: ApplicationConfig): DataSource {
    val props = newDataSourceProps(appConfig.config("application.datasource.authServerConsole"))
    return newDataSource(props)
}

private fun newAuthServerDataSource(appConfig: ApplicationConfig): DataSource {
    val props = newDataSourceProps(appConfig.config("application.datasource.authServer"))
    return newDataSource(props)
}

private fun newDataSourceProps(dataSourceConfig: ApplicationConfig) = Properties()
    .apply {
        setProperty("dataSourceClassName", dataSourceConfig.property("dataSourceClassName").getString())
        setProperty("dataSource.user", dataSourceConfig.property("user").getString())
        setProperty("dataSource.password", dataSourceConfig.property("password").getString())
        setProperty("dataSource.databaseName", dataSourceConfig.property("databaseName").getString())
        setProperty("dataSource.portNumber", dataSourceConfig.property("portNumber").getString())
        setProperty("dataSource.serverName", dataSourceConfig.property("serverName").getString())
    }

private fun newDataSource(properties: Properties): DataSource = HikariDataSource(HikariConfig(properties))