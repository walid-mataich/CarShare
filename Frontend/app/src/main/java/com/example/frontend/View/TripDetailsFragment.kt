package com.example.frontend.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.adapter.ReservationAdapter2
import com.example.frontend.model.ReservationTrip
import com.example.frontend.model.Trip
import com.example.frontend.ViewModel.ReservationViewModel
import com.google.android.material.snackbar.Snackbar
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TripDetailsFragment : Fragment() {

    private lateinit var trip: Trip
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var reservationAdapter: ReservationAdapter2

    companion object {
        fun newInstance(trip: Trip): TripDetailsFragment {
            val fragment = TripDetailsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("trip", trip)
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trip = requireArguments().getParcelable("trip")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trip_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Informations du trip
        view.findViewById<TextView>(R.id.txtRoute).text =
            "${trip.originAddress} ➝ ${trip.destinationAddress}"

        view.findViewById<TextView>(R.id.txtDriver).text =
            "Conducteur : ${trip.driverName}"

        val zdt = ZonedDateTime.parse(trip.departureTime)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")
        view.findViewById<TextView>(R.id.txtDate).text =
            "Départ : ${zdt.format(formatter)}"

        view.findViewById<TextView>(R.id.txtPrice).text =
            "Prix : ${trip.price} DH"

        view.findViewById<TextView>(R.id.txtSeats).text =
            "Places totales : ${trip.availableSeats}"

        val txtRemainingSeats = view.findViewById<TextView>(R.id.txtRemainingSeats)
        txtRemainingSeats.text =
            "Places restantes : ${trip.placeRestant}"

        view.findViewById<TextView>(R.id.txtDistance).text =
            "Distance : ${calculateDistance()} km"

        if (trip.placeRestant == 0) {
            txtRemainingSeats.text = "COMPLET"
            txtRemainingSeats.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red_700)
            )
        }

        view.findViewById<Button>(R.id.btnViewRoute).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, TripRouteMapFragment.newInstance(trip))
                .addToBackStack(null)
                .commit()
        }

        // ---------------- RecyclerView pour les réservations ----------------
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvReservations)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        reservationAdapter = ReservationAdapter2(listOf()) { reservation ->
            // Callback pour accepter la réservation
            acceptReservation(reservation)
        }
        recyclerView.adapter = reservationAdapter

        // ---------------- ViewModel pour charger les réservations ----------------
        reservationViewModel = ViewModelProvider(this)[ReservationViewModel::class.java]
        reservationViewModel.loadReservationsForTrip(trip.id)
        reservationViewModel.reservations.observe(viewLifecycleOwner) { reservations ->
            reservationAdapter = ReservationAdapter2(reservations) { reservation ->
                acceptReservation(reservation)
            }
            recyclerView.adapter = reservationAdapter
        }
    }

    private fun calculateDistance(): Int {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            trip.originLat,
            trip.originLng,
            trip.destinationLat,
            trip.destinationLng,
            results
        )
        return (results[0] / 1000).toInt()
    }

    private fun acceptReservation(reservation: ReservationTrip) {
        if (reservation.seatsRequested <= trip.placeRestant) {
            // Appeler le ViewModel pour accepter la réservation
            reservationViewModel.acceptReservation(reservation.id)
        } else {
            // Message si pas assez de places
            Toast.makeText(
                requireContext(),
                "Impossible d’accepter : pas assez de places disponibles",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
