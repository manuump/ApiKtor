package com.example.ktor

import com.example.domain.models.Usuario
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.RegisterUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {


    routing {
        // Ruta de prueba
        get("/") {
            call.respondText("Backend inicado!")
        }

        // Rutas estáticas
        staticResources("/static", "static")

        // Rutas de usuarios
        usuarioRoutes(registerUseCase, loginUseCase)
    }
}

// Definición de las rutas de usuarios
fun Route.usuarioRoutes(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {
    route("/usuarios") {
        post("/register") {
            val usuario = call.receive<Usuario>()
            val createdUsuario = registerUseCase(usuario)
            call.respond(HttpStatusCode.Created, createdUsuario)
        }

        post("/login") {
            val usuario = call.receive<Usuario>()
            val loggedInUsuario = loginUseCase(usuario.username, usuario.password)
            if (loggedInUsuario != null) {
                call.respond(HttpStatusCode.OK, "Has iniciado sesion con exito")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
            }
        }
    }
}
