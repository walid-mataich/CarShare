package com.example.frontend.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.frontend.R
import com.example.frontend.ViewModel.CreateTripViewModel
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.LocationRequest
import com.example.frontend.model.TripRequest
import java.util.Calendar

class CreateTripActivity : ComponentActivity() {

    private lateinit var viewModel: CreateTripViewModel

    private lateinit var driverIdInput: EditText
    private lateinit var dateTimeInput: EditText
    private lateinit var seatsInput: EditText
    private lateinit var priceInput: EditText

    private lateinit var originBtn: Button
    private lateinit var destinationBtn: Button
    private lateinit var originText: TextView
    private lateinit var destinationText: TextView
    private lateinit var submitBtn: Button

    private var originLocation: LocationRequest? = null
    private var destinationLocation: LocationRequest? = null
    private var currentPlaceResult: ((LocationRequest) -> Unit)? = null

    private var isoDateTime: String = "" // ISO 8601 envoyé au backend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitInstance.init(this)
        setContentView(R.layout.activity_create_trip)

        viewModel = ViewModelProvider(this)[CreateTripViewModel::class.java]

        bindViews()
        setupObservers()
        setupListeners()
    }

    // --------------------
    // INITIALISATION VUES
    // --------------------
    private fun bindViews() {
        driverIdInput = findViewById(R.id.driverIdInput)
        dateTimeInput = findViewById(R.id.dateTimeInput)
        seatsInput = findViewById(R.id.seatsInput)
        priceInput = findViewById(R.id.priceInput)

        originBtn = findViewById(R.id.originBtn)
        destinationBtn = findViewById(R.id.destinationBtn)
        originText = findViewById(R.id.originText)
        destinationText = findViewById(R.id.destinationText)
        submitBtn = findViewById(R.id.submitBtn)
    }

    // --------------------
    // MVVM OBSERVERS
    // --------------------
    private fun setupObservers() {
        viewModel.success.observe(this) {
            Toast.makeText(this, "Trajet créé avec succès", Toast.LENGTH_LONG).show()
        }

        viewModel.error.observe(this) { msg ->
            Toast.makeText(this, "$msg", Toast.LENGTH_LONG).show()
        }
    }

    // --------------------
    // LISTENERS UI
    // --------------------
    private fun setupListeners() {
        dateTimeInput.setOnClickListener { pickDateTime() }

        originBtn.setOnClickListener {
            pickPlace { location ->
                originLocation = location
                originText.text = location.address
            }
        }

        destinationBtn.setOnClickListener {
            pickPlace { location ->
                destinationLocation = location
                destinationText.text = location.address
            }
        }

        submitBtn.setOnClickListener { submitForm() }
    }

    // --------------------
    // PICKER DATE + HEURE
    // --------------------
    private fun pickDateTime() {
        val now = Calendar.getInstance()

        DatePickerDialog(this,
            { _, y, m, d ->
                TimePickerDialog(this,
                    { _, h, min ->
                        // ISO 8601 pour backend
                        isoDateTime = String.format(
                            "%04d-%02d-%02dT%02d:%02d:00Z",
                            y, m + 1, d, h, min
                        )
                        // Affichage lisible pour l'utilisateur
                        val display = String.format("%02d/%02d/%04d %02d:%02d", d, m + 1, y, h, min)
                        dateTimeInput.setText(display)
                    },
                    now[Calendar.HOUR_OF_DAY],
                    now[Calendar.MINUTE],
                    true
                ).show()
            },
            now[Calendar.YEAR],
            now[Calendar.MONTH],
            now[Calendar.DAY_OF_MONTH]
        ).show()
    }

    // --------------------
    // OUVERTURE DE LA MAP
    // --------------------
    private fun pickPlace(result: (LocationRequest) -> Unit) {
        currentPlaceResult = result
        val intent = Intent(this, MapsActivity::class.java)
        placeLauncher.launch(intent)
    }

    // --------------------
    // RESULTAT MAP
    // --------------------
    private val placeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == RESULT_OK && res.data != null) {
                val lat = res.data!!.getDoubleExtra("lat", 0.0)
                val lng = res.data!!.getDoubleExtra("lng", 0.0)
                val address = res.data!!.getStringExtra("address") ?: ""
                val location = LocationRequest(lat, lng, address)
                currentPlaceResult?.invoke(location)
            }
        }

    // --------------------
    // SOUMISSION FORMULAIRE
    // --------------------
    private fun submitForm() {
        val driverId = driverIdInput.text.toString().toLongOrNull()
        val seats = seatsInput.text.toString().toIntOrNull()
        val price = priceInput.text.toString().toDoubleOrNull()

        if (driverId == null ||
            originLocation == null ||
            destinationLocation == null ||
            isoDateTime.isBlank() ||
            seats == null ||
            price == null
        ) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            return
        }

        val trip = TripRequest(
            driverId = driverId,
            origin = originLocation!!,
            destination = destinationLocation!!,
            departureTime = isoDateTime, // ISO 8601 envoyé au backend
            availableSeats = seats,
            price = price
        )

        viewModel.createTrip(trip)
    }
}
