package com.example.domain.repository

import com.example.domain.models.Evento
import com.example.domain.models.Eventos
import com.example.repository.EventoRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PersistenceEventoRepository : EventoRepository {

    override suspend fun createEvento(evento: Evento): Evento {
        return transaction {
            val id = Eventos.insert {
                it[titulo] = evento.titulo
                it[descripcion] = evento.descripcion
                it[imagen] = evento.imagen
            } get Eventos.id

            evento.copy(id = id)
        }
    }

    override suspend fun getEventoById(id: Int): Evento? {
        return transaction {
            Eventos.selectAll().where { Eventos.id eq id }
                .map {
                    Evento(
                        id = it[Eventos.id],
                        titulo = it[Eventos.titulo],
                        descripcion = it[Eventos.descripcion],
                        imagen = it[Eventos.imagen]
                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun getAllEventos(): List<Evento> {
        return transaction {
            Eventos.selectAll().map {
                Evento(
                    id = it[Eventos.id],
                    titulo = it[Eventos.titulo],
                    descripcion = it[Eventos.descripcion],
                    imagen = it[Eventos.imagen]
                )
            }
        }
    }

    override suspend fun updateEvento(id: Int, evento: Evento): Boolean {
        return transaction {
            val rowsUpdated = Eventos.update({ Eventos.id eq id }) {
                it[titulo] = evento.titulo
                it[descripcion] = evento.descripcion
                it[imagen] = evento.imagen
            }
            rowsUpdated > 0
        }
    }

    override suspend fun deleteEvento(id: Int): Boolean {
        return transaction {
            val rowsDeleted = Eventos.deleteWhere { Eventos.id eq id }
            rowsDeleted > 0
        }
    }
}

