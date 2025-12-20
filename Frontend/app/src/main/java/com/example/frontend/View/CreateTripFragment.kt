package com.example.frontend.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.frontend.R
import com.example.frontend.ViewModel.CreateTripViewModel
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.LocationRequest
import com.example.frontend.model.TripRequest
import java.util.Calendar

class CreateTripFragment : Fragment() {

    private lateinit var viewModel: CreateTripViewModel
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

    private var isoDateTime: String = ""

    // --------------------
    // LIFECYCLE
    // --------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        RetrofitInstance.init(requireContext())
        return inflater.inflate(R.layout.activity_create_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CreateTripViewModel::class.java]

        bindViews(view)
        setupObservers()
        setupListeners()
    }

    // --------------------
    // INITIALISATION VUES
    // --------------------
    private fun bindViews(view: View) {
        dateTimeInput = view.findViewById(R.id.dateTimeInput)
        seatsInput = view.findViewById(R.id.seatsInput)
        priceInput = view.findViewById(R.id.priceInput)
        originBtn = view.findViewById(R.id.originBtn)
        destinationBtn = view.findViewById(R.id.destinationBtn)
        originText = view.findViewById(R.id.originText)
        destinationText = view.findViewById(R.id.destinationText)
        submitBtn = view.findViewById(R.id.submitBtn)
    }

    // --------------------
    // OBSERVERS MVVM
    // --------------------
    private fun setupObservers() {
        viewModel.success.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Trajet créé avec succès", Toast.LENGTH_LONG).show()
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
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

        DatePickerDialog(requireContext(),
            { _, y, m, d ->
                TimePickerDialog(requireContext(),
                    { _, h, min ->
                        isoDateTime = String.format(
                            "%04d-%02d-%02dT%02d:%02d:00Z",
                            y, m + 1, d, h, min
                        )

                        val display = String.format(
                            "%02d/%02d/%04d %02d:%02d",
                            d, m + 1, y, h, min
                        )

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
    // OUVERTURE MAP
    // --------------------
    private fun pickPlace(result: (LocationRequest) -> Unit) {
        currentPlaceResult = result
        val intent = Intent(requireContext(), MapsActivity::class.java)
        placeLauncher.launch(intent)
    }

    // --------------------
    // RESULTAT MAP
    // --------------------
    private val placeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == android.app.Activity.RESULT_OK && res.data != null) {
                val lat = res.data!!.getDoubleExtra("lat", 0.0)
                val lng = res.data!!.getDoubleExtra("lng", 0.0)
                val address = res.data!!.getStringExtra("address") ?: ""
                val location = LocationRequest(lat, lng, address)
                currentPlaceResult?.invoke(location)
            }
        }

    // --------------------
    // SUBMIT
    // --------------------
    private fun submitForm() {
        val seats = seatsInput.text.toString().toIntOrNull()
        val price = priceInput.text.toString().toDoubleOrNull()

        if (originLocation == null ||
            destinationLocation == null ||
            isoDateTime.isBlank() ||
            seats == null ||
            price == null
        ) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            return
        }

        val trip = TripRequest(
            driverId = 0,
            origin = originLocation!!,
            destination = destinationLocation!!,
            departureTime = isoDateTime,
            availableSeats = seats,
            price = price
        )

        viewModel.createTrip(trip)
    }
}
