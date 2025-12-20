package com.example.frontend.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.ReservationTrip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationViewModel : ViewModel() {

    private val _reservations = MutableLiveData<List<ReservationTrip>>()
    val reservations: LiveData<List<ReservationTrip>> = _reservations

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Charger les réservations pour un trajet donné
    fun loadReservationsForTrip(tripId: Long) {
        RetrofitInstance.apiInterface.getReservationsForTrip(tripId)
            .enqueue(object : Callback<List<ReservationTrip>> {
                override fun onResponse(
                    call: Call<List<ReservationTrip>>,
                    response: Response<List<ReservationTrip>>
                ) {
                    if (response.isSuccessful) {
                        _reservations.value = response.body()
                    } else {
                        _error.value = "Erreur de chargement des réservations"
                    }
                }

                override fun onFailure(call: Call<List<ReservationTrip>>, t: Throwable) {
                    _error.value = t.message
                }
            })
    }

    // Accepter une réservation
    fun acceptReservation(reservationId: Long) {
        RetrofitInstance.apiInterface.acceptReservation(reservationId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Mettre à jour la liste après acceptation
                        _reservations.value = _reservations.value?.map {
                            if (it.id == reservationId) it.copy(status = "ACCEPTED") else it
                        }
                    } else {
                        _error.value = "Impossible d'accepter la réservation"
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    _error.value = t.message
                }
            })
    }
}
