package com.example.ktor


import com.example.domain.repository.PersistenceEventoRepository
import com.example.domain.repository.PersistenceUsuarioRepository
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.RegisterUseCase
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    configureSerialization()
    configureDatabases()

    val eventoRepository = PersistenceEventoRepository()
    val usuarioRepository = PersistenceUsuarioRepository()
    val registerUseCase = RegisterUseCase(usuarioRepository)
    val loginUseCase = LoginUseCase(usuarioRepository)

    // Configura las rutas
    configureRouting(registerUseCase, loginUseCase, eventoRepository)
}
