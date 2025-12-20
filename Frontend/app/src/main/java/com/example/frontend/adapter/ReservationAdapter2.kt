package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.model.ReservationTrip

class ReservationAdapter2(
    private val reservations: List<ReservationTrip>,
    private val onAcceptClick: (ReservationTrip) -> Unit
) : RecyclerView.Adapter<ReservationAdapter2.ReservationViewHolder>() {

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtRequesterName: TextView = itemView.findViewById(R.id.txtRequesterName)
        val txtRequesterEmail: TextView = itemView.findViewById(R.id.txtRequesterEmail)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val txtSeatsRequested: TextView = itemView.findViewById(R.id.txtSeatsRequested)
        val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
        val btnAccept: Button = itemView.findViewById(R.id.btnAcceptReservation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation2, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]

        // Infos du requester
        holder.txtRequesterName.text = reservation.requester?.name ?: "Nom inconnu"
        holder.txtRequesterEmail.text = reservation.requester?.email ?: ""

        // Infos de la reservation
        holder.txtStatus.text = "Statut: ${reservation.status}"
        holder.txtSeatsRequested.text = "Places demandées: ${reservation.seatsRequested}"
        holder.txtAmount.text = "Montant: ${reservation.amount} €"

        // Bouton accepter visible seulement si le statut est "PENDING"
        holder.btnAccept.visibility = if (reservation.status.equals("PENDING", ignoreCase = true)) {
            View.VISIBLE
        } else {
            View.GONE
        }

        // Callback du bouton
        holder.btnAccept.setOnClickListener {
            onAcceptClick(reservation)
        }
    }

    override fun getItemCount(): Int = reservations.size
}
