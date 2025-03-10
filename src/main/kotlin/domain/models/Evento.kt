package com.example.domain.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Evento(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String,
    val imagen: String
)

object Eventos : Table("eventos") {
    val id = integer("id").autoIncrement()
    val titulo = varchar("titulo", 255)
    val descripcion = text("descripcion")
    val imagen = text("imagen")

    override val primaryKey = PrimaryKey(id)
}