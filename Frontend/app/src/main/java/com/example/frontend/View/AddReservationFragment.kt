package com.example.frontend.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.adapter.TripsAdapter
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.Trip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReservationFragment : Fragment() {

    private lateinit var adapter: TripsAdapter

    /** Liste complète (source) */
    private val allTrips = mutableListOf<Trip>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.tripsRecyclerView)

        // 🔹 Ici on crée l'adapter et on définit le callback pour chaque trip cliqué
        adapter = TripsAdapter(allTrips) { trip ->

            val fragment = AddReservationConfirmFragment()
            val bundle = Bundle()
            bundle.putParcelable("trip", trip)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, fragment) // ID de ton container principal
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadAllTrips()
        setupFilters(view)
    }


    private fun loadAllTrips() {
        RetrofitInstance.apiInterface.getAllTrips()
            .enqueue(object : Callback<List<Trip>> {

                override fun onResponse(
                    call: Call<List<Trip>>,
                    response: Response<List<Trip>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            allTrips.clear()
                            allTrips.addAll(it)
                            adapter.updateData(allTrips)
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erreur lors du chargement des trajets",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Erreur réseau : ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun setupFilters(view: View) {

        val edtOrigin = view.findViewById<EditText>(R.id.edtOrigin)
        val edtDestination = view.findViewById<EditText>(R.id.edtDestination)
        val edtDriver = view.findViewById<EditText>(R.id.edtDriver)
        val edtSeats = view.findViewById<EditText>(R.id.edtSeats)
        val edtMaxPrice = view.findViewById<EditText>(R.id.edtMaxPrice)
        val btnFilter = view.findViewById<Button>(R.id.btnFilter)

        btnFilter.setOnClickListener {

            val requiredSeats = edtSeats.text.toString().toIntOrNull()
            val maxPrice = edtMaxPrice.text.toString().toDoubleOrNull()
            val origin = edtOrigin.text.toString()
            val destination = edtDestination.text.toString()
            val driver = edtDriver.text.toString()

            val filtered = allTrips.filter { trip ->

                val seatsOk =
                    requiredSeats == null || trip.placeRestant >= requiredSeats

                val priceOk =
                    maxPrice == null || trip.price <= maxPrice

                val originOk =
                    origin.isBlank() || trip.originAddress.contains(origin, true)

                val destinationOk =
                    destination.isBlank() || trip.destinationAddress.contains(destination, true)

                val driverOk =
                    driver.isBlank() || trip.driverName.contains(driver, true)

                seatsOk && priceOk && originOk && destinationOk && driverOk
            }

            adapter.updateData(filtered)

            if (filtered.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Aucun trajet ne correspond aux filtres",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
