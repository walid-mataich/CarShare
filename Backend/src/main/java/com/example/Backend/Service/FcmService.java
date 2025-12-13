package com.example.Backend.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {
    public void sendNotification(String fcmToken, String title, String body){
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();

        try { FirebaseMessaging.getInstance().send(message); }
        catch(Exception e){ e.printStackTrace(); }
    }
}
