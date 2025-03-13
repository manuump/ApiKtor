package com.example.ktor

import com.example.domain.models.Evento
import com.example.domain.models.Usuario
import com.example.domain.repository.UsuarioRepository
import com.example.domain.usecase.*
import com.example.repository.EventoRepository
import domain.security.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase,
    eventoRepository: EventoRepository,
    usuarioRepository: UsuarioRepository
) {
    val getAllEventosUseCase = GetAllEventosUseCase(eventoRepository)
    val getEventoByIdUseCase = GetEventoByIdUseCase(eventoRepository)
    val createEventoUseCase = CreateEventoUseCase(eventoRepository)
    val updateEventoUseCase = UpdateEventoUseCase(eventoRepository)
    val deleteEventoUseCase = DeleteEventoUseCase(eventoRepository)

    routing {
        // Ruta de prueba
        get("/") {
            call.respondText("Backend en funcionamiento")
        }

        // Rutas de usuarios (No requieren autenticación)
        usuarioRoutes(registerUseCase, loginUseCase)

        // Rutas de eventos (Protegidas con JWT)
        authenticate {
            eventoRoutes(
                getAllEventosUseCase,
                getEventoByIdUseCase,
                createEventoUseCase,
                updateEventoUseCase,
                deleteEventoUseCase,
                usuarioRepository
            )
        }
    }
}

// Rutas de Usuarios
fun Route.usuarioRoutes(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {
    route("/usuarios") {
        post("/register") {
            val usuario = call.receive<Usuario>()
            val createdUsuario = registerUseCase(usuario)
            println("Usuario registrado: $createdUsuario")
            call.respond(HttpStatusCode.Created, createdUsuario)
        }

        post("/login") {
            val usuario = call.receive<Usuario>()
            val token = loginUseCase(usuario.username, usuario.password)
            if (token != null) {
                println("Login exitoso. Token generado: $token")
                call.respond(HttpStatusCode.OK, mapOf("token" to token))
            } else {
                println("Login fallido. Credenciales inválidas")
                call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
            }
        }
    }
}

// Rutas de Eventos (Protegidas con JWT)
fun Route.eventoRoutes(
    getAllEventosUseCase: GetAllEventosUseCase,
    getEventoByIdUseCase: GetEventoByIdUseCase,
    createEventoUseCase: CreateEventoUseCase,
    updateEventoUseCase: UpdateEventoUseCase,
    deleteEventoUseCase: DeleteEventoUseCase,
    usuarioRepository: UsuarioRepository
) {
    route("/eventos") {
        // Obtener todos los eventos
        get {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt()

            if (userId != null) {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ")

                if (token != null) {
                    val usuario = usuarioRepository.getUsuarioById(userId)
                    if (usuario != null && usuario.token == token) {
                        val eventos = getAllEventosUseCase()
                        println("Eventos obtenidos: $eventos")
                        call.respond(HttpStatusCode.OK, eventos)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Token inválido")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Token no proporcionado")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido")
            }
        }

        // Obtener un evento por ID
        get("/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt()

            if (userId != null) {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ")

                if (token != null) {
                    val usuario = usuarioRepository.getUsuarioById(userId)
                    if (usuario != null && usuario.token == token) {
                        val id = call.parameters["id"]?.toIntOrNull()
                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@get
                        }
                        val evento = getEventoByIdUseCase(id)
                        if (evento != null) {
                            println("Evento encontrado: $evento")
                            call.respond(HttpStatusCode.OK, evento)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Evento no encontrado")
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Token inválido")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Token no proporcionado")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido")
            }
        }

        // Crear un nuevo evento
        post {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt()

            if (userId != null) {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ")

                if (token != null) {
                    val usuario = usuarioRepository.getUsuarioById(userId)
                    if (usuario != null && usuario.token == token) {
                        val evento = call.receive<Evento>()
                        val nuevoEvento = createEventoUseCase(evento)
                        println("Evento creado: $nuevoEvento")
                        call.respond(HttpStatusCode.Created, nuevoEvento)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Token inválido")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Token no proporcionado")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido")
            }
        }

        // Actualizar un evento
        put("/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt()

            if (userId != null) {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ")

                if (token != null) {
                    val usuario = usuarioRepository.getUsuarioById(userId)
                    if (usuario != null && usuario.token == token) {
                        val id = call.parameters["id"]?.toIntOrNull()
                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@put
                        }
                        val evento = call.receive<Evento>()
                        val actualizado = updateEventoUseCase(id, evento)
                        if (actualizado) {
                            println("Evento actualizado con éxito")
                            call.respond(HttpStatusCode.OK, "Evento actualizado con éxito")
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Evento no encontrado")
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Token inválido")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Token no proporcionado")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido")
            }
        }

        // Eliminar un evento
        delete("/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt()

            if (userId != null) {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ")

                if (token != null) {
                    val usuario = usuarioRepository.getUsuarioById(userId)
                    if (usuario != null && usuario.token == token) {
                        val id = call.parameters["id"]?.toIntOrNull()
                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@delete
                        }
                        val eliminado = deleteEventoUseCase(id)
                        if (eliminado) {
                            println("Evento eliminado con éxito")
                            call.respond(HttpStatusCode.OK, "Evento eliminado con éxito")
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Evento no encontrado")
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Token inválido")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Token no proporcionado")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido")
            }
        }
    }

}