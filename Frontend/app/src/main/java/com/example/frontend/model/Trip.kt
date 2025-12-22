package com.example.frontend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(
    val id: Long,
    val originAddress: String,
    val destinationAddress: String,
    val originLat: Double,
    val originLng: Double,
    val destinationLat: Double,
    val destinationLng: Double,
    val driverName: String,
    val departureTime: String,
    val price: Double,
    val availableSeats: Int,
    val placeRestant:Int
) : Parcelable
