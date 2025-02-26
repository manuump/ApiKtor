package com.example.domain.usecase

import com.example.repository.EventoRepository

class DeleteEventoUseCase(private val eventoRepository: EventoRepository) {
    suspend operator fun invoke(id: Int): Boolean = eventoRepository.deleteEvento(id)
}