package com.example.repository

import com.example.domain.models.Evento

interface EventoRepository {
    suspend fun getAllEventos(): List<Evento>
    suspend fun getEventoById(id: Int): Evento?
    suspend fun createEvento(evento: Evento): Evento
    suspend fun updateEvento(id: Int, evento: Evento): Boolean
    suspend fun deleteEvento(id: Int): Boolean
}