package com.example.frontend.model

import java.time.Instant

data class ReservationTrip(
    val id: Long,
    val status: String,
    val requestedAt: String?,
    val respondedAt: String?,
    val seatsRequested: Int,
    val amount: Double,
    val trip: Trip?,
    val requester: User?
)
