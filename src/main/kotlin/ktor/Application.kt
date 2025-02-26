package com.example.ktor


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

    val usuarioRepository = PersistenceUsuarioRepository()
    val registerUseCase = RegisterUseCase(usuarioRepository)
    val loginUseCase = LoginUseCase(usuarioRepository)

    configureRouting(registerUseCase, loginUseCase)
}
