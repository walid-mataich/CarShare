package com.example.frontend.model

data class ReservationRequest(
    val tripId: Long,
    val seatsRequested: Int,
    val prix:Double
)
