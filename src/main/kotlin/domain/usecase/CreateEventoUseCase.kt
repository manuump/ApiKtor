package com.example.domain.usecase

import com.example.domain.models.Evento
import com.example.repository.EventoRepository

class CreateEventoUseCase(private val eventoRepository: EventoRepository) {
    suspend operator fun invoke(evento: Evento): Evento {
        return eventoRepository.createEvento(evento)
    }
}