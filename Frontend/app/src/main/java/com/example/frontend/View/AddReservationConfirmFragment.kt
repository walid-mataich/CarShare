package com.example.frontend.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.Trip
import com.example.frontend.model.ReservationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AddReservationConfirmFragment : Fragment() {

    private lateinit var trip: Trip
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trip = requireArguments().getParcelable("trip")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_confirm_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val txtRoute = view.findViewById<TextView>(R.id.txtRoute)
        val txtDriver = view.findViewById<TextView>(R.id.txtDriver)
        val txtDeparture = view.findViewById<TextView>(R.id.txtDeparture)
        val txtSeatsTotal = view.findViewById<TextView>(R.id.txtSeats)
        val txtRemainingSeats = view.findViewById<TextView>(R.id.txtRemainingSeats)
        val txtUnitPrice = view.findViewById<TextView>(R.id.txtUnitPrice)
        val txtPrice = view.findViewById<TextView>(R.id.txtTotalPrice)
        val edtSeats = view.findViewById<EditText>(R.id.edtSeats)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)

        // Champs non modifiables
        listOf(txtRoute, txtDriver, txtDeparture, txtSeatsTotal, txtRemainingSeats, txtUnitPrice).forEach {
            it.isFocusable = false
            it.isClickable = false
        }

        // Affichage des informations du trip
        txtRoute.text = "${trip.originAddress} ➝ ${trip.destinationAddress}"
        txtDriver.text = "Conducteur : ${trip.driverName}"
        txtDeparture.text = try {
            val dt = ZonedDateTime.parse(trip.departureTime)
            "Départ : ${dt.format(dateFormatter)}"
        } catch (e: Exception) {
            "Départ : ${trip.departureTime}"
        }
        txtSeatsTotal.text = "Places totales : ${trip.availableSeats}"
        txtRemainingSeats.text = "Places restantes : ${trip.placeRestant}"
        txtUnitPrice.text = "Prix unitaire : ${trip.price} DH"
        txtPrice.text = "Prix total : 0 DH"

        // Calcul dynamique du prix total
        edtSeats.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val seats = s.toString().toIntOrNull()
                if (seats != null && seats > 0) {
                    if (seats > trip.placeRestant) {
                        edtSeats.error = "Places insuffisantes"
                        txtPrice.text = "Prix total : 0 DH"
                        btnConfirm.isEnabled = false
                    } else {
                        val totalPrice = trip.price * seats
                        txtPrice.text = "Prix total : $totalPrice DH"
                        btnConfirm.isEnabled = true
                    }
                } else {
                    txtPrice.text = "Prix total : 0 DH"
                    btnConfirm.isEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Confirmation de la réservation
        btnConfirm.setOnClickListener {
            val seats = edtSeats.text.toString().toIntOrNull()
            if (seats == null || seats <= 0 || seats > trip.placeRestant) {
                Toast.makeText(requireContext(), "Nombre de places invalide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reservationRequest = ReservationRequest(
                tripId = trip.id,
                seatsRequested = seats,
                prix = trip.price * seats
            )

            RetrofitInstance.apiInterface.createReservation(reservationRequest)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Réservation effectuée ! Total: ${trip.price * seats} DH",
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().supportFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Erreur lors de la réservation",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Erreur réseau : ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        // Désactiver le bouton si aucune place saisie
        btnConfirm.isEnabled = false
    }
}
