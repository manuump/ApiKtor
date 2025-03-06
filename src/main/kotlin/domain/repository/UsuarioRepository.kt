package com.example.domain.repository

import com.example.domain.models.Usuario

interface UsuarioRepository {
    suspend fun createUsuario(usuario: Usuario): Usuario
    suspend fun getUsuarioByUsername(username: String): Usuario?
    suspend fun updateToken(userId: Int, token: String)
    suspend fun getUsuarioById(userId: Int): Usuario?

}