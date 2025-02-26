package com.example.domain.usecase

import com.example.domain.models.Usuario
import com.example.domain.repository.UsuarioRepository
import domain.security.PasswordHash

class RegisterUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(usuario: Usuario): Usuario {
        val hashedPassword = PasswordHash.hash(usuario.password)
        val usuarioConHash = usuario.copy(password = hashedPassword)
        return repository.createUsuario(usuarioConHash)
    }
}
