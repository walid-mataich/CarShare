package com.example.frontend.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.Reservation
import com.example.frontend.model.Trip
import retrofit2.*
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ReservationDetailsFragment : Fragment() {

    companion object {
        private const val ARG_RESERVATION = "reservation"

        fun newInstance(reservation: Reservation): ReservationDetailsFragment {
            return ReservationDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RESERVATION, reservation)
                }
            }
        }
    }

    private lateinit var reservation: Reservation
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reservation = requireArguments().getParcelable(ARG_RESERVATION)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_reservation_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val txtTrip = view.findViewById<TextView>(R.id.txtTrip)
        val txtStatus = view.findViewById<TextView>(R.id.txtStatus)
        val txtSeats = view.findViewById<TextView>(R.id.txtSeats)
        val txtDriver = view.findViewById<TextView>(R.id.txtDriver)
        val txtAmount = view.findViewById<TextView>(R.id.txtAmount)
        val txtDeparture = view.findViewById<TextView>(R.id.txtDeparture)
        val txtRequestedAt2 = view.findViewById<TextView>(R.id.txtRequestedAt2)
        val txtRespondedAt2 = view.findViewById<TextView>(R.id.txtRespondedAt2)
        val btnShowMap = view.findViewById<Button>(R.id.btnShowMap)

        txtTrip.text = "${reservation.tripOrigin} ➝ ${reservation.tripDestination}"
        txtStatus.text = "Statut : ${reservation.status}"
        txtSeats.text = "Places réservées : ${reservation.seatsRequested}"
        txtDriver.text = "Conducteur : ${reservation.driverName ?: "-"}"
        txtAmount.text = "Montant : ${reservation.amount} DH"
        txtDeparture.text = formatDate("Départ", reservation.departureTime)
        txtRequestedAt2.text = formatDate("Demandée le", reservation.requestedAt)
        txtRespondedAt2.text = reservation.respondedAt?.let { formatDate("Répondu le", it) } ?: "Répondu le : -"

        btnShowMap.setOnClickListener { openMap() }
    }

    private fun formatDate(label: String, date: String): String {
        return try {
            val zdt = ZonedDateTime.parse(date)
            "$label : ${zdt.format(formatter)}"
        } catch (e: Exception) {
            "$label : $date"
        }
    }

    private fun openMap() {
        val tripId = reservation.tripId ?: run {
            Toast.makeText(requireContext(), "ID du trajet manquant", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.apiInterface.getTripById(tripId).enqueue(object : Callback<Trip> {
            override fun onResponse(call: Call<Trip>, response: Response<Trip>) {
                if (response.isSuccessful) {
                    val trip = response.body()
                    trip?.let {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.home_fragment_container, TripRouteMapFragment.newInstance(it))
                            .addToBackStack(null)
                            .commit()
                    } ?: Toast.makeText(requireContext(), "Trip vide", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Trip non trouvé", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Trip>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
