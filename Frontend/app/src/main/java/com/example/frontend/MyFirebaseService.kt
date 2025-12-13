package com.example.frontend

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("MyFirebaseService", "Message received: $remoteMessage")

        remoteMessage.notification?.let {
            showNotification(it.title ?: "New message", it.body ?: "")
        }
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel"
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.car) // use a valid icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(0, notification)
    }
}