package com.example.ktor


import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory


fun Application.configureDatabases() {
    val log = LoggerFactory.getLogger("DatabaseConfig")

    val driver = environment.config.property("ktor.database.driver").getString()
    val url = environment.config.property("ktor.database.url").getString()
    val username = environment.config.property("ktor.database.username").getString()
    val password = environment.config.property("ktor.database.password").getString()

    try {
        Database.connect(
            url = url,
            driver = driver,
            user = username,
            password = password
        )
        log.info("Conexión a la base de datos establecida correctamente.")
    } catch (e: Exception) {
        log.error("Error al conectar a la base de datos: ${e.message}")
    }
}