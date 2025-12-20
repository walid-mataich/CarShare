package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.model.Trip
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TripsAdapter(private var trips: List<Trip>, private val onItemClick: (Trip) -> Unit) : RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {

    class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val route: TextView = view.findViewById(R.id.txtRoute)
        val driver: TextView = view.findViewById(R.id.txtDriver)
        val departure: TextView = view.findViewById(R.id.txtDeparture)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val seats: TextView = view.findViewById(R.id.txtSeats)
        val remainingSeats: TextView = view.findViewById(R.id.txtRemainingSeats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]

        holder.route.text =
            "${trip.originAddress} ➝ ${trip.destinationAddress}"

        holder.driver.text = "Conducteur : ${trip.driverName}"

        val zdt = ZonedDateTime.parse(trip.departureTime)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")
        holder.departure.text = "Départ : ${zdt.format(formatter)}"

        holder.price.text = "Prix : ${trip.price} DH"
        holder.seats.text = "Places totales : ${trip.availableSeats}"

        holder.remainingSeats.text =
            "Places restantes : ${trip.placeRestant}"

        holder.itemView.setOnClickListener {
            onItemClick(trip)
        }
    }
    override fun getItemCount() = trips.size

    fun updateData(newTrips: List<Trip>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}
