package com.example.domain.usecase

import com.example.domain.models.Usuario
import com.example.domain.repository.UsuarioRepository
import domain.security.PasswordHash

class LoginUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(username: String, password: String): Usuario? {
        val usuario = repository.getUsuarioByUsername(username)
        return if (usuario != null && PasswordHash.verify(password, usuario.password)) { // Comparar con el hash
            usuario
        } else {
            null
        }
    }
}
