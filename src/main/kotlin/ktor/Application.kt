package com.example.ktor

import com.example.domain.repository.PersistenceEventoRepository
import com.example.domain.repository.PersistenceUsuarioRepository
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.RegisterUseCase
import domain.security.JWTConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

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

    // Configura la autenticación JWT
    install(Authentication) {
        jwt {
            verifier(JWTConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }


    // Configura las rutas
    configureRouting(registerUseCase, loginUseCase, eventoRepository ,usuarioRepository )
}