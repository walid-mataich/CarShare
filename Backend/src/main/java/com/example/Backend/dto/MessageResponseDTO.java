package com.example.Backend.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class MessageResponseDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Instant sentAt;
    private boolean read;
}