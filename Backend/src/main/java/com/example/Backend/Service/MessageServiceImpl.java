package com.example.Backend.Service;

import com.example.Backend.Model.Message;
//import com.example.Backend.Model.User;
import com.example.Backend.Model.User;
import com.example.Backend.Repository.MessageRepository;
import com.example.Backend.Repository.TripRepository;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MessageServiceImpl {
    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final FcmService fcmService;

    public Message saveMessage(ChatMessageDTO dto, String senderEmail){
        User sender = userRepo.findByEmail(senderEmail).orElseThrow();
        User receiver = userRepo.findById(dto.getReceiverId()).orElseThrow();

        Message msg = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .sentAt(Instant.now())
                .read(false)
                .build();

        Message saved = messageRepo.save(msg);

        if(receiver.getFcmToken() != null){
            fcmService.sendNotification(
                    receiver.getFcmToken(),
                    "New message from " + sender.getName(),
                    dto.getContent()
            );
        }

        return saved;
    }

    public List<Message> getConversation(Long userId1, Long userId2){
        return messageRepo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(
                userId1, userId2, userId1, userId2
        );
    }

    public void markAsRead(Long messageId){
        Message msg = messageRepo.findById(messageId).orElseThrow();
        msg.setRead(true);
        messageRepo.save(msg);
    }

    public void updateFcmToken(String senderEmail, String token){
        User user = userRepo.findByEmail(senderEmail).orElseThrow();
        user.setFcmToken(token);
        userRepo.save(user);
    }
}
