package com.example.frontend.model


data class ConversationItem(
    val userId: Long,
    val username: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: String
)

