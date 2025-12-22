package com.example.Backend.Controller;

import com.example.Backend.Model.Message;
import com.example.Backend.Service.MessageService;
import com.example.Backend.Service.MessageServiceImpl;
import com.example.Backend.Service.TokenBlacklistService;
import com.example.Backend.Service.UserServiceImpl;
import com.example.Backend.dto.ChatMessageDTO;
import com.example.Backend.dto.ConversationResponseDTO;
import com.example.Backend.dto.MessageResponseDTO;
import com.example.Backend.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user/chat")
@RequiredArgsConstructor
public class MessageController {
    private final MessageServiceImpl messageService;
    private final UserServiceImpl userService;


    @PostMapping("/send")
    public MessageResponseDTO send(@RequestBody ChatMessageDTO dto, Principal principal){
        String senderEmail = String.valueOf(principal.getName());
        System.out.println("Sender Email: " + senderEmail);
        Message saved = messageService.saveMessage(dto, senderEmail);
        return toDto(saved);
    }


    @GetMapping("/conversation/{userId}")
    public List<MessageResponseDTO> getConversation(@PathVariable Long userId, Principal principal){
        String email = String.valueOf(principal.getName());
        return messageService.getConversation(email, userId).stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/conversations")
    public List<ConversationResponseDTO> getMyConversations(Principal principal) {
        String email = String.valueOf(principal.getName());
        return messageService.getMyConversations(email);
    }

    @PostMapping("/read/{messageId}")
    public void markRead(@PathVariable Long messageId){
        messageService.markAsRead(messageId);
    }

    @PostMapping("/fcm-token")
    public void updateFcmToken(@RequestParam String token, Principal principal){
        String senderEmail = String.valueOf(principal.getName());
        messageService.updateFcmToken(senderEmail, token);
    }

    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers(Principal principal) {
        String email = principal.getName();

        return userService.getAllUsersExcept(email);
    }




    private MessageResponseDTO toDto(Message msg){
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(msg.getId());
        dto.setSenderId(msg.getSender().getId());
        dto.setReceiverId(msg.getReceiver().getId());
        dto.setContent(msg.getContent());
        dto.setSentAt(msg.getSentAt());
        dto.setRead(msg.isRead());
        return dto;
    }
}

