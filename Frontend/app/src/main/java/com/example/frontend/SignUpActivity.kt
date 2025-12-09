package com.example.frontend

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import java.util.Calendar


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Récupérer les éléments du layout
        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        val loginRedirect = findViewById<TextView>(R.id.loginRedirect)

        val dobInput = findViewById<EditText>(R.id.dobInput)

        dobInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                // Formater la date en dd/MM/yyyy
                val selectedDate = "${d.toString().padStart(2,'0')}/${(m+1).toString().padStart(2,'0')}/$y"
                dobInput.setText(selectedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Action du bouton d'inscription
        signUpBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show()
                // Ici tu peux ajouter la logique pour enregistrer l'utilisateur dans une base de données
            }
        }

        // Redirection vers la page de connexion

    }
}