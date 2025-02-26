package com.example.domain.repository

import com.example.domain.models.Usuario

interface UsuarioRepository {
    suspend fun createUsuario(usuario: Usuario): Usuario
    suspend fun getUsuarioByUsername(username: String): Usuario?
}