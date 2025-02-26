package com.example.domain.repository

import com.example.domain.models.Usuario
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PersistenceUsuarioRepository : UsuarioRepository {
    override suspend fun createUsuario(usuario: Usuario): Usuario {
        return transaction {
            val id = Usuarios.insert {
                it[username] = usuario.username
                it[password] = usuario.password
            } get Usuarios.id

            usuario.copy(id = id)
        }
    }

    override suspend fun getUsuarioByUsername(username: String): Usuario? {
        return transaction {
            Usuarios.selectAll().where { Usuarios.username eq username }
                .map {
                    Usuario(
                        id = it[Usuarios.id],
                        username = it[Usuarios.username],
                        password = it[Usuarios.password]
                    )
                }
                .singleOrNull()
        }
    }
}

object Usuarios : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(id)
}