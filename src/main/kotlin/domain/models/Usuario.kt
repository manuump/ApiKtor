package com.example.domain.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Usuario(
    val id: Int? = null,
    val username: String,
    val password: String
)

object Usuarios : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(id)
}