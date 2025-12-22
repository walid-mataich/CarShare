package com.example.frontend.model

data class SendMessageRequest(
    val receiverId: Long,
    val content: String
)
