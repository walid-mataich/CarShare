package com.example.frontend.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.ViewModel.MyTripsViewModel
import com.example.frontend.adapter.LastItemBottomMarginDecoration
import com.example.frontend.adapter.TripsAdapter

class MyTripsFragment : Fragment() {

    private lateinit var viewModel: MyTripsViewModel
    private lateinit var adapter: TripsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MyTripsViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.tripsRecyclerView)
        val createBtn = view.findViewById<Button>(R.id.btnCreateTrip)

        adapter = TripsAdapter(emptyList()) { trip ->
            val fragment = TripDetailsFragment.newInstance(trip)
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            LastItemBottomMarginDecoration(lastItemExtraMarginDp = 48)
        )

        createBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, CreateTripFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.trips.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.loadMyTrips()
    }
}
