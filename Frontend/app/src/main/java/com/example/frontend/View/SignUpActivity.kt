package com.example.frontend.View

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.ViewModel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val dobInput = findViewById<EditText>(R.id.dobInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        val loginRedirect = findViewById<TextView>(R.id.loginRedirect)

        val calendar = Calendar.getInstance()

        // Date picker for DOB
        dobInput.setOnClickListener {
            val datePicker = android.app.DatePickerDialog(this,
                { _, y, m, d ->
                    val selectedDate = "${d.toString().padStart(2,'0')}/${(m+1).toString().padStart(2,'0')}/$y"
                    dobInput.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Signup button
        signUpBtn.setOnClickListener {
            val name = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val dob = dobInput.text.toString().trim()

            if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || dob.isEmpty()) {
                Snackbar.make(it, "Veuillez remplir tous les champs", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = calculateAge(dob)
            if(age < 18){
                Snackbar.make(it, "Vous devez avoir au moins 18 ans", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call ViewModel
            authViewModel.register(name, email, phone, age, password)
        }

        // Observe signup response
        authViewModel.signUpResponse.observe(this) { response ->
            response ?: return@observe

            if(response.statusCode == 200){
                // Modern popup using Snackbar
                Snackbar.make(signUpBtn, "Inscription réussie ! Bienvenue ${usernameInput.text}", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.teal_700))
                    .setTextColor(resources.getColor(R.color.white))
                    .setAction("Continuer") {
                        // Redirect to login page
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }.show()
            } else {
                Snackbar.make(signUpBtn, "Mot de pass ou email incorrecte", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.red_700))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()
            }
        }

        // Redirect to login if user taps link
        // Récupérer le TextView


// Ajouter le click listener
        loginRedirect.setOnClickListener {
            // Créer l'intent pour aller vers LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Optionnel : fermer l'activité SignUp pour ne pas revenir en arrière avec "Back"
            finish()
        }
    }

    private fun calculateAge(dobString: String): Int {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dob = sdf.parse(dobString)
            val dobCalendar = Calendar.getInstance().apply { time = dob!! }
            val today = Calendar.getInstance()
            var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
            if(today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) age--
            age
        } catch (e: Exception){
            0
        }
    }
}
