package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val username: String,
    val password: String
)