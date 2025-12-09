package com.example.frontend.api

import com.example.frontend.model.ResponseData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("/test")
    fun getData(): Call<ResponseData>
}