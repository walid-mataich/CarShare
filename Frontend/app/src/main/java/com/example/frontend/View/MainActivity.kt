package com.example.frontend.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.R
import com.example.frontend.View.SignUpActivity
import com.example.frontend.api.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, you can show notifications
                println("Notification permission granted")
            } else {
                println("Notification permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        println("test")

        // Initialize Retrofit if needed
        RetrofitInstance.init(this)

        // Get FCM token
//        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
//            println("FCM TOKEN = $token")
//            // Send token to backend
//            sendTokenToBackend(token)
//        }


        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    println("Notification permission already granted")
                }

                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

        }



        val getStartedBtn = findViewById<Button>(R.id.getStartedBtn)

        getStartedBtn.setOnClickListener {
            // Redirection vers la page d'inscription
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


    }

//    private fun sendTokenToBackend(token: String) {
//        // Use CoroutineScope with Dispatchers.IO
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                RetrofitInstance.apiInterface.updateFcmToken(token)
//                println("FCM token sent to backend successfully")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}