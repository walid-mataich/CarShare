package com.example.Backend.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long receiverId;
    private String content;
}
