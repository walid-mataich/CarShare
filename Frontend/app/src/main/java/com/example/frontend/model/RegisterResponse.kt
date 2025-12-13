package com.example.frontend.model

data class RegisterResponse(
    val statusCode: Int?,
    val message: String?,
    val error: String?,
    val token: String?,
    val refreshToken: String?,
    val expirationTime: String?,
    val email: String?,
    val nom: String?,
    val age: Int?
)
