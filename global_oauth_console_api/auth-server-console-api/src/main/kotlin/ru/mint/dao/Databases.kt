package ru.mint.dao

import org.jetbrains.exposed.sql.Database

data class Databases(val authServer: Database, val authServerConsole: Database)
