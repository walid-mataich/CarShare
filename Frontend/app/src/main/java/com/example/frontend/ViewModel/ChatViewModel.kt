package com.example.frontend.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.Repository.ChatRepository
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.ConversationItem
import com.example.frontend.model.Message
import com.example.frontend.model.UserItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class ChatViewModel: ViewModel() {

    private val repository = ChatRepository()
    private val _conversations = MutableLiveData<List<ConversationItem>>()
    val conversations: LiveData<List<ConversationItem>> = _conversations

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _sentMessage = MutableLiveData<Message>()
    val sentMessage: LiveData<Message> = _sentMessage

    fun loadConversations() {
        viewModelScope.launch {
            _conversations.value = repository.getConversations()
        }
    }

    fun loadConversation(userId: Long) {
        viewModelScope.launch {
            _messages.value = repository.getConversation(userId)
        }
    }

    fun sendMessage(userId: Long, content: String) {
        // create a temporary message with a negative id to avoid collision
        val tempId = -Random.nextLong(1, Long.MAX_VALUE)
        val tempMsg = Message(
            id = tempId,
            senderId = 0L,            // any value != conversationUserId so adapter treats it as "sent"
            receiverId = userId,
            content = content,
            sentAt = java.time.Instant.now().toString()
        )

        // append locally (create a new list instance)
        _messages.value = (_messages.value ?: emptyList()) + tempMsg

        viewModelScope.launch {
            try {
                val realMsg = repository.sendMessage(userId, content)
                // replace temp with real message (match by tempId)
                _messages.value = _messages.value?.map { if (it.id == tempId) realMsg else it }
                _sentMessage.value = realMsg
            } catch (e: Exception) {
                // on error remove temp or mark failed (simple approach: remove)
                _messages.value = _messages.value?.filter { it.id != tempId }
                // optionally expose error to UI
            }
        }
    }


    private val _users = MutableLiveData<List<UserItem>>()
    val users: LiveData<List<UserItem>> = _users

    suspend fun getAllUsers(): List<UserItem> {
        val response = repository.getAllUsers()
        return response
    }








}