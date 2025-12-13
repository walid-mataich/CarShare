package com.example.frontend.api

import com.example.frontend.model.LoginRequest
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.RegisterResponse
import com.example.frontend.model.ResponseData
import com.example.frontend.model.TripRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("/test")
    fun getData(): Call<ResponseData>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): RegisterResponse

    @POST("user/chat/fcm-token")
    suspend fun updateFcmToken(@Query("token") token: String)


    @POST("auth/trips")
    fun createTrip(@Body trip: TripRequest): Call<Void>
}