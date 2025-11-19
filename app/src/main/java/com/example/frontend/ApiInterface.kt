package com.example.frontend


import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("/test")
    fun getData(): Call<ResponseData>
}