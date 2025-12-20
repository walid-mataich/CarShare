package com.example.frontend.model

data class Message(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val sentAt: String
)
