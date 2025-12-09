package com.example.frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

import android.widget.ImageView
import android.widget.Toast
import com.example.frontend.R // Ajoutez cette ligne
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getStartedBtn = findViewById<Button>(R.id.getStartedBtn)

        getStartedBtn.setOnClickListener {
            // Redirection vers la page d'inscription
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


    }

}