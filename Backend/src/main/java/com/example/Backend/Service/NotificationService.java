package com.example.Backend.Service;

import com.example.Backend.Model.Notification;

import java.util.List;

public interface NotificationService {
    Notification send(Notification notification);
    void markDelivered(Long notificationId);

    Notification findById(Long id);
    List<Notification> findByUser(Long userId);
}
