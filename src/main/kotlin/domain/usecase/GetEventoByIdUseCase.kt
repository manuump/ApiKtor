package com.example.domain.usecase

import com.example.domain.models.Evento
import com.example.repository.EventoRepository

class GetEventoByIdUseCase(private val eventoRepository: EventoRepository) {
    suspend operator fun invoke(id: Int): Evento? {
        return eventoRepository.getEventoById(id)
    }
}