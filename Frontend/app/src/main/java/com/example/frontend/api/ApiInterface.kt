package com.example.frontend.api

import com.example.frontend.model.ConversationItem
import com.example.frontend.model.LoginRequest
import com.example.frontend.model.Message
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.RegisterResponse
import com.example.frontend.model.Reservation
import com.example.frontend.model.ReservationRequest
import com.example.frontend.model.ReservationTrip
import com.example.frontend.model.ResponseData
import com.example.frontend.model.Trip
import com.example.frontend.model.SendMessageRequest
import com.example.frontend.model.TripRequest
import com.example.frontend.model.UserItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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


    @POST("/user/trips/create")
    fun createTrip(@Body trip: TripRequest): Call<Void>

    @GET("/user/chat/conversations")
    suspend fun getMyConversations(): List<ConversationItem>

    @GET("/user/chat/conversation/{userId}")
    suspend fun getConversation(
        @Path("userId") userId: Long
    ): List<Message>

    @POST("/user/chat/send")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): Message


    @POST("user/logout")
    suspend fun logout(): Response<Unit>


    @GET("/user/chat/users")
    suspend fun getAllUsers(): List<UserItem>


    @GET("/user/trips/my")
    fun getMyTrips(): Call<List<Trip>>

    @GET("/user/trips")
    fun getAllTrips(): Call<List<Trip>>

    @GET("/user/reservations/my")
    fun getMyReservations(): Call<List<Reservation>>

    @POST("/user/reservations/{id}/cancel")
    fun cancelReservation(@Path("id") reservationId: Long): Call<Void>

    @POST("/user/reservations/add")
    fun createReservation(@Body request: ReservationRequest): Call<Void>

    @GET("/user/reservations/trip/{tripId}")
    fun getReservationsForTrip(@Path("tripId") tripId: Long): Call<List<ReservationTrip>>

    @POST("/user/reservations/{reservationId}/accept")
    fun acceptReservation(@Path("reservationId") reservationId: Long): Call<Void>
    @GET("/user/trips/{id}")
    fun getTripById(@Path("id") id: Long): Call<Trip>


}