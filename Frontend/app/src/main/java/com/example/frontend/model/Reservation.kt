package com.example.frontend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Reservation(
    val id: Long,
    val status: String,
    val requestedAt: String,
    val respondedAt: String?,
    val seatsRequested: Int,
    val amount: Double,
    val tripId: Long,
    val tripOrigin: String,
    val tripDestination: String,
    val driverName: String,
    val driverId: Long,
    val departureTime: String
) : Parcelable
