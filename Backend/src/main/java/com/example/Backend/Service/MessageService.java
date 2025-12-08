package com.example.Backend.Service;

import com.example.Backend.Model.Message;

import java.util.List;

public interface MessageService {
    Message send(Message message);
    void markAsRead(Long messageId);

    Message findById(Long id);
    List<Message> getConversation(Long user1, Long user2);
    List<Message> getInbox(Long userId);
}
