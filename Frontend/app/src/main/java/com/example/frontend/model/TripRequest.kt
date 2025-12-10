package com.example.frontend.model

data class TripRequest(
    val driverId: Long,
    val origin: LocationRequest,
    val destination: LocationRequest,
    val departureTime: String,
    val availableSeats: Int,
    val price: Double
)
