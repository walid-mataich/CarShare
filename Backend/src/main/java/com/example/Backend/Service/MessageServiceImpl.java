package com.example.Backend.Service;

import com.example.Backend.Model.Message;
//import com.example.Backend.Model.User;
import com.example.Backend.Model.User;
import com.example.Backend.Repository.MessageRepository;
import com.example.Backend.Repository.TripRepository;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.dto.ChatMessageDTO;
import com.example.Backend.dto.ConversationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Message> getConversation(String userEmail1, Long userId2){
        Long userId1 = userRepo.findByEmail(userEmail1).orElseThrow().getId();
        return messageRepo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(
                userId1, userId2, userId1, userId2
        );
    }


    public List<ConversationResponseDTO> getMyConversations(String email) {

        Long myId = userRepo.findByEmail(email).orElseThrow().getId();

        List<Message> messages = messageRepo.findAllByUser(myId);

        Map<Long, Message> lastMessagesPerUser = new HashMap<>();

        for (Message msg : messages) {
            Long otherUserId =
                    msg.getSender().getId().equals(myId)
                            ? msg.getReceiver().getId()
                            : msg.getSender().getId();

            if (!lastMessagesPerUser.containsKey(otherUserId)
                    || msg.getSentAt().isAfter(
                    lastMessagesPerUser.get(otherUserId).getSentAt())) {

                lastMessagesPerUser.put(otherUserId, msg);
            }
        }

        return lastMessagesPerUser.entrySet().stream()
                .map(entry -> {
                    Message msg = entry.getValue();
                    User otherUser =
                            msg.getSender().getId().equals(myId)
                                    ? msg.getReceiver()
                                    : msg.getSender();

                    return ConversationResponseDTO.builder()
                            .userId(otherUser.getId())
                            .username(otherUser.getEmail())
                            .name(otherUser.getName())// or username
                            .lastMessage(msg.getContent())
                            .lastMessageTime(msg.getSentAt())
                            .build();
                })
                .sorted(Comparator.comparing(
                        ConversationResponseDTO::getLastMessageTime).reversed())
                .toList();
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
