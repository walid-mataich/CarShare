package com.example.frontend.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.*
import retrofit2.*

class CreateTripViewModel : ViewModel() {

    val success = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun createTrip(trip: TripRequest) {

        RetrofitInstance.apiInterface
            .createTrip(trip)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    if (response.isSuccessful) {
                        success.postValue(true)
                    } else {
                        error.postValue("Erreur: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    error.postValue(t.localizedMessage)
                }
            })
    }
}
