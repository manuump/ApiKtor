package com.example.domain.usecase

import com.example.domain.models.Evento
import com.example.repository.EventoRepository

class UpdateEventoUseCase(private val eventoRepository: EventoRepository) {
    suspend operator fun invoke(id: Int, evento: Evento): Boolean =
        eventoRepository.updateEvento(id, evento)
}