package com.example.frontend.model

data class TripRequest(
    val driverId: Long? = null,
    val origin: LocationRequest? = null,
    val destination: LocationRequest? = null,
    val departureTime: String? = null,
    val availableSeats: Int? = null,
    val price: Double? = null
)
