package com.example.frontend.model

data class User(
    val id: Long,
    val name: String?,
    val email: String?,
    val phone: String? = null,
    val role: String? = null
)
