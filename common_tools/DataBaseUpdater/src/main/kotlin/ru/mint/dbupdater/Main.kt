package ru.mint.dbupdater

import com.fasterxml.jackson.module.kotlin.*
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import kotlin.system.exitProcess
import org.slf4j.LoggerFactory

private var currentDbVersion: String = ""
private var updaterConfig: UpdaterConfig? = null
private val log = LoggerFactory.getLogger("[DB Updater]")

fun main() {
    loadUpdaterConfig()
    getCurrentDbVersion()
    val scriptBundleList = getScriptBundles()
    if (scriptBundleList.isEmpty()) {
        log.info("===>>> DB up to date!")
        exitProcess(-1)
    }

    val connection = getConnection()
    for (scriptBundle in scriptBundleList) {
        preloadAndExecuteScript(connection, scriptBundle)
    }
    getCurrentDbVersion()
    connection.close()
}

fun loadUpdaterConfig() {
    try {
        val mapper = jacksonObjectMapper()
        mapper.registerKotlinModule()
        val jsonContent: String = File("./updater_config.json").readText(Charsets.UTF_8)
        updaterConfig = mapper.readValue(jsonContent)
    } catch (e: Exception) {
        log.error("===>>> Loading 'updater_config.json' error: $e\nStopping a process.")
        exitProcess(-1)
    }
}

fun getConnection(): Connection {
    val connection = DriverManager.getConnection(updaterConfig!!.dbUrl, updaterConfig!!.dbUser, updaterConfig!!.dbPassword)
    if (connection.isValid(0)) {
        connection.autoCommit = true
        return connection
    } else {
        log.error("===>>> DbConnection is not valid. Stopping a process.")
        exitProcess(-1)
    }
}

fun getCurrentDbVersion() {
    val queryFindTable = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE  table_name   = 'version');"
    val queryVersion = "SELECT v.version FROM version v ORDER BY v.creation_date DESC LIMIT 1;"
    val connection = getConnection()

    try {
        val dbFindTableQuery = connection.prepareStatement(queryFindTable)
        val queryFindResult = dbFindTableQuery.executeQuery()
        if (queryFindResult.next()) {
            val tableExist = queryFindResult.getBoolean("exists")
            if (!tableExist) {
                log.info("===>>> Table 'version' not found.")
                connection.close()
                currentDbVersion = ""
                return
            }
        } else {
            connection.close()
            currentDbVersion = ""
            return
        }
    } catch (e: Exception) {
        log.error("===>>> Error finding 'version' table.\nException: $e.\nStopping a process.")
        exitProcess(-1)
    }

    try {
        val dbVersionQuery = connection.prepareStatement(queryVersion)
        val queryResult = dbVersionQuery.executeQuery()
        if (queryResult.next()) {
            currentDbVersion = queryResult.getString("version")
            log.info("===>>> Current DB version: $currentDbVersion")
        }
        connection.close()
    } catch (e: Exception) {
        log.error("===>>> Error getting DB version.\nException: $e\nStopping a process.")
        exitProcess(-1)
    }
}

fun getScriptBundles(): List<ScriptsBundle> {
    var elementIndex = 0
    var elementFounded = false
    var scriptsBundleList = mutableListOf<ScriptsBundle>()
    try {
        val mapper = jacksonObjectMapper()
        mapper.registerKotlinModule()
        val jsonContent: String = File("${updaterConfig!!.baseDir}/db_bundles.json").readText(Charsets.UTF_8)
        val structuredScriptsBundleList: List<StructuredScriptBundle> = mapper.readValue(jsonContent)

        for (structuredScriptsBundle in structuredScriptsBundleList) {
            val directory = structuredScriptsBundle.directory
            val fileList = structuredScriptsBundle.files
            for (file in fileList) {
                scriptsBundleList.add(ScriptsBundle(directory = directory, file = "$file.sql", version = file))
            }
        }

        if (currentDbVersion.isNotBlank()) {
            for (scriptsBundle in scriptsBundleList) {
                elementIndex++
                if (scriptsBundle.version == currentDbVersion) {
                    elementFounded = true
                    break
                }
            }
        }

        return if (elementFounded)
            scriptsBundleList.subList(elementIndex, scriptsBundleList.count())
        else
            scriptsBundleList.subList(0, scriptsBundleList.count())
    } catch (e: Exception) {
        log.error("===>>> Loading 'db_bundles.json' error: $e\nStopping a process.")
        exitProcess(-1)
    }
}

fun preloadAndExecuteScript(connection: Connection, scriptsBundle: ScriptsBundle) {
    val file = File("${updaterConfig!!.baseDir}/${scriptsBundle.directory}/${scriptsBundle.file}")
    if (!file.exists()) {
        log.error("===>>> File ${file.name} is not exists.\nStopping a process.")
        exitProcess(-1)
    }

    val fileContent = file.readLines()
    var query = ""
    fileContent.forEach {
        query += it + "\n"
    }

    if (connection.isValid(0)) {
        try {
            log.info("===>>> Preparing query from file [${updaterConfig!!.baseDir}/${scriptsBundle.directory}/${scriptsBundle.file}]")
            val dbVersionQuery = connection.prepareStatement(query)
            dbVersionQuery.execute()
            log.info("===>>> Query from file [${updaterConfig!!.baseDir}/${scriptsBundle.directory}/${scriptsBundle.file}] successfully completed!")
        } catch (e: Exception) {
            log.error("===>>> Error executing query [${updaterConfig!!.baseDir}/${scriptsBundle.directory}/${scriptsBundle.file}].\nException: $e\nStopping a process.")
            exitProcess(-1)
        }
    } else {
        log.error("===>>> DbConnection is not valid. Stopping a process.")
        exitProcess(-1)
    }
}
