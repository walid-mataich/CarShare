package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationResponseDTO {
    private Long userId;
    private String username;
    private String lastMessage;
    private Instant lastMessageTime;
}