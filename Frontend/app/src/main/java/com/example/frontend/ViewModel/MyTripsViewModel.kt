package com.example.frontend.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.Trip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTripsViewModel : ViewModel() {

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadMyTrips() {
        RetrofitInstance.apiInterface.getMyTrips().enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful) {
                    _trips.value = response.body()
                } else {
                    _error.value = "Erreur de chargement"
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                _error.value = t.message
            }
        })
    }
}
