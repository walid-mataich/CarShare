package com.example.frontend.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.adapter.LastItemBottomMarginDecoration
import com.example.frontend.adapter.ReservationsAdapter
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.Reservation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReservationsFragment : Fragment() {

    private lateinit var adapter: ReservationsAdapter
    private val reservations = mutableListOf<Reservation>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.reservationsRecyclerView)
        val btnAddReservation = view.findViewById<Button>(R.id.btnAddReservation)

        adapter = ReservationsAdapter(
            reservations,
            onItemClick = { reservation ->
                openReservationDetails(reservation)
            },
            onCancelClick = { reservation ->
                cancelReservation(reservation)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
            LastItemBottomMarginDecoration(48)
        )

        // ✅ Ouvrir le fragment d'ajout de réservation
        btnAddReservation.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, AddReservationFragment())
                .addToBackStack(null)
                .commit()
        }

        loadReservations()
    }


    private fun loadReservations() {
        RetrofitInstance.apiInterface.getMyReservations().enqueue(object : Callback<List<Reservation>> {
            override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateData(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun openReservationDetails(reservation: Reservation) {
        val fragment = ReservationDetailsFragment.newInstance(reservation)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun cancelReservation(reservation: Reservation) {
        RetrofitInstance.apiInterface.cancelReservation(reservation.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Réservation annulée", Toast.LENGTH_SHORT).show()
                    loadReservations()
                } else {
                    Toast.makeText(requireContext(), "Impossible d'annuler", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
