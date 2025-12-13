package com.example.frontend.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R

class MainActivityTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test)

        val tokenText = findViewById<TextView>(R.id.tokenText)
        val userInfoText = findViewById<TextView>(R.id.userInfoText)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        // Récupérer le token et infos utilisateur depuis SharedPreferences
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "Aucun token trouvé")
        val userName = sharedPref.getString("USER_NAME", "Nom inconnu")
        val userEmail = sharedPref.getString("USER_EMAIL", "Email inconnu")

        tokenText.text = "Token : $token"
        userInfoText.text = "Nom : $userName\nEmail : $userEmail"

        // Déconnexion
        logoutBtn.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
