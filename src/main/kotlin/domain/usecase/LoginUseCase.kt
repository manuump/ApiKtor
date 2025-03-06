package com.example.domain.usecase

import com.example.domain.repository.UsuarioRepository
import domain.security.JWTConfig
import domain.security.PasswordHash

class LoginUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(username: String, password: String): String? {
        val usuario = repository.getUsuarioByUsername(username)
        return if (usuario != null && PasswordHash.verify(password, usuario.password)) {
            val newToken = JWTConfig.generateToken(usuario.id!!, usuario.username)
            repository.updateToken(usuario.id, newToken)
            newToken
        } else {
            null
        }
    }
}