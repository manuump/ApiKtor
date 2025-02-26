package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Evento (
    val id: Int? = null,
    val tiutlo: String,
    val descripcion: String
)