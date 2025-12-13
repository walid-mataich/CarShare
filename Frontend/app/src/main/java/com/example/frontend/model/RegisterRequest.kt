package com.example.frontend.model

data class RegisterRequest(
    val nom: String,
    val email: String,
    val age: Int,
    val password: String,
    val phone: String
)