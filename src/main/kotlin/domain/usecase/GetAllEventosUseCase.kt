package com.example.domain.usecase

import com.example.domain.models.Evento
import com.example.repository.EventoRepository

class GetAllEventosUseCase(private val eventoRepository: EventoRepository) {
    suspend operator fun invoke(): List<Evento> = eventoRepository.getAllEventos()
}