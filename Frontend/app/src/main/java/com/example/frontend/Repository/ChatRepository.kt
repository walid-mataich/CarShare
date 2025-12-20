package com.example.frontend.Repository

import android.util.Log
import com.example.frontend.api.ApiInterface
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.ConversationItem
import com.example.frontend.model.Message
import com.example.frontend.model.SendMessageRequest
import com.example.frontend.model.UserItem

class ChatRepository {


    suspend fun getConversations(): List<ConversationItem> {
        return RetrofitInstance.apiInterface.getMyConversations()
    }

    suspend fun getConversation(userId: Long): List<Message> {
        return RetrofitInstance.apiInterface.getConversation(userId)
            .sortedBy { it.sentAt }
    }

    suspend fun sendMessage(
        receiverId: Long,
        content: String
    ): Message {
        return RetrofitInstance.apiInterface.sendMessage(
            SendMessageRequest(receiverId, content)
        )
    }


    suspend fun getAllUsers(): List<UserItem> {
        val response = RetrofitInstance.apiInterface.getAllUsers()
        return RetrofitInstance.apiInterface.getAllUsers()
    }
}
