package com.example.frontend.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.ViewModel.AuthViewModel
import com.example.frontend.api.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        authViewModel.repository.setContext(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupRedirect = findViewById<TextView>(R.id.signupRedirect)

        // Login button
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()){
                Snackbar.make(loginBtn, "Veuillez remplir tous les champs", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call ViewModel login function
            authViewModel.login(email, password)
        }

        // Observe login response
        authViewModel.loginResponse.observe(this) { response ->
            response ?: return@observe

            if(response.statusCode == 200){
                Snackbar.make(loginBtn, "Connexion réussie ! Bienvenue", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.teal_700))
                    .setTextColor(resources.getColor(R.color.white))

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()

                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                    println("FCM TOKEN = $token")
                    // Send token to backend
                    sendTokenToBackend(token)
                }
            } else {
                Snackbar.make(loginBtn, "Erreur: ${response.message ?: response.error}", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.red_700))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()
            }
        }

        // Redirect to signup page
        signupRedirect.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun sendTokenToBackend(token: String) {
        // Use CoroutineScope with Dispatchers.IO
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitInstance.apiInterface.updateFcmToken(token)
                println("FCM token sent to backend successfully")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
