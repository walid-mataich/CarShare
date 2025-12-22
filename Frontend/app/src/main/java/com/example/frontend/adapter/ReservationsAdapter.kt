package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.model.Reservation
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ReservationsAdapter(
    private var reservations: List<Reservation>,
    private val onItemClick: (Reservation) -> Unit,
    private val onCancelClick: (Reservation) -> Unit,
    private val onContactDriverClick: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTrip: TextView = view.findViewById(R.id.txtTrip)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val txtSeats: TextView = view.findViewById(R.id.txtSeats)
        val txtDriver: TextView = view.findViewById(R.id.txtDriver)
        val txtDeparture: TextView = view.findViewById(R.id.txtDeparture)
        val txtAmount: TextView = view.findViewById(R.id.txtAmountres)
        val txtRequestedAt: TextView = view.findViewById(R.id.txtRequestedAtres)
        val txtRespondedAt: TextView = view.findViewById(R.id.txtRespondedAtres)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        val btnContactDriver: Button = view.findViewById(R.id.btnContactDriver)
    }

    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]

        // Route
        holder.txtTrip.text = "${reservation.tripOrigin} ➝ ${reservation.tripDestination}"

        // Statut
        holder.txtStatus.text = "Statut: ${reservation.status}"
        if (reservation.status == "CANCELLED") {
            holder.txtStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.status_cancelled)
            )
        } else {
            holder.txtStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.status_default)
            )
        }

        // Places
        holder.txtSeats.text = "Places: ${reservation.seatsRequested}"

        // Conducteur
        holder.txtDriver.text = "Conducteur: ${reservation.driverName}"

        // Heure de départ
        holder.txtDeparture.text = try {
            val dt = ZonedDateTime.parse(reservation.departureTime)
            "Départ: ${dt.format(formatter)}"
        } catch (e: Exception) {
            "Départ: ${reservation.departureTime}"
        }

        // Prix total
        holder.txtAmount.text = "Prix total: ${reservation.amount} DH"

        // Dates demandées et répondues
        holder.txtRequestedAt.text = try {
            val dt = ZonedDateTime.parse(reservation.requestedAt)
            "Demandé le: ${dt.format(formatter)}"
        } catch (e: Exception) {
            "Demandé le: ${reservation.requestedAt}"
        }

        holder.txtRespondedAt.text = reservation.respondedAt?.let {
            try {
                val dt = ZonedDateTime.parse(it)
                "Répondu le: ${dt.format(formatter)}"
            } catch (e: Exception) {
                "Répondu le: $it"
            }
        } ?: "Répondu le: -"

        // Bouton annuler
        holder.btnCancel.isEnabled = reservation.status == "PENDING" || reservation.status == "ACCEPTED"
        holder.btnCancel.setOnClickListener {
            onCancelClick(reservation)
        }

        holder.btnContactDriver.setOnClickListener { onContactDriverClick(reservation) } // NEW


        holder.itemView.setOnClickListener {
            onItemClick(reservation)
        }

    }

    override fun getItemCount(): Int = reservations.size

    fun updateData(newReservations: List<Reservation>) {
        reservations = newReservations
        notifyDataSetChanged()
    }
}
