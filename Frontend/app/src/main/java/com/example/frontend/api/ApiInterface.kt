package com.example.frontend.api

import com.example.frontend.model.ResponseData
import com.example.frontend.model.TripRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("/test")
    fun getData(): Call<ResponseData>

    @POST("/trips")
    fun createTrip(@Body trip: TripRequest): Call<Void>
}