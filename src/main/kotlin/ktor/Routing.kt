package com.example.ktor

import com.example.domain.models.Evento
import com.example.domain.models.Usuario
import com.example.domain.usecase.*
import com.example.repository.EventoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase,
    eventoRepository: EventoRepository
) {
    val getAllEventosUseCase = GetAllEventosUseCase(eventoRepository)
    val getEventoByIdUseCase = GetEventoByIdUseCase(eventoRepository)
    val createEventoUseCase = CreateEventoUseCase(eventoRepository)
    val updateEventoUseCase = UpdateEventoUseCase(eventoRepository)
    val deleteEventoUseCase = DeleteEventoUseCase(eventoRepository)

    routing {
        // Ruta de prueba
        get("/") {
            call.respondText("Backend iniciado!")
        }

        // Rutas estáticas
        staticResources("/static", "static")

        // Rutas de usuarios
        usuarioRoutes(registerUseCase, loginUseCase)

        // Rutas de eventos
        eventoRoutes(
            getAllEventosUseCase,
            getEventoByIdUseCase,
            createEventoUseCase,
            updateEventoUseCase,
            deleteEventoUseCase
        )
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
                call.respond(HttpStatusCode.OK, "Has iniciado sesión con éxito")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
            }
        }
    }
}

// Definición de las rutas de eventos
fun Route.eventoRoutes(
    getAllEventosUseCase: GetAllEventosUseCase,
    getEventoByIdUseCase: GetEventoByIdUseCase,
    createEventoUseCase: CreateEventoUseCase,
    updateEventoUseCase: UpdateEventoUseCase,
    deleteEventoUseCase: DeleteEventoUseCase
) {
    route("/eventos") {
        // Obtener todos los eventos
        get {
            val eventos = getAllEventosUseCase()
            call.respond(eventos)
        }

        // Obtener un evento por ID
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }
            val evento = getEventoByIdUseCase(id)
            if (evento == null) {
                call.respond(HttpStatusCode.NotFound, "Evento no encontrado")
            } else {
                call.respond(evento)
            }
        }

        // Crear un nuevo evento
        post("/add") {
            val evento = call.receive<Evento>()
            val nuevoEvento = createEventoUseCase(evento)
            call.respond(HttpStatusCode.Created, nuevoEvento)
        }

        // Actualizar un evento
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }
            val evento = call.receive<Evento>()
            val actualizado = updateEventoUseCase(id, evento)
            if (actualizado) {
                call.respond(HttpStatusCode.OK, "Evento actualizado")
            } else {
                call.respond(HttpStatusCode.NotFound, "Evento no encontrado")
            }
        }

        // Eliminar un evento
        delete("/del/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@delete
            }
            val eliminado = deleteEventoUseCase(id)
            if (eliminado) {
                call.respond(HttpStatusCode.OK, "Evento eliminado")
            } else {
                call.respond(HttpStatusCode.NotFound, "Evento no encontrado")
            }
        }
    }
}