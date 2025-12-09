package com.example.frontend.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _data = MutableLiveData<ResponseData>()
    val data: LiveData<ResponseData> = _data

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun loadData() {

        RetrofitInstance.apiInterface.getData()
            .enqueue(object : Callback<ResponseData> {

                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _data.postValue(response.body())
                    } else {
                        _error.postValue("Erreur serveur : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    _error.postValue(t.localizedMessage)
                }
            })
    }
}
