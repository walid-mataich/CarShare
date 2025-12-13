package com.example.frontend.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.Repository.AuthRepository
import com.example.frontend.model.LoginRequest
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.RegisterResponse
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {


     val repository = AuthRepository()

    // LiveData à observer depuis l'Activity
    val signUpResponse = MutableLiveData<RegisterResponse?>()

    fun register(nom: String, email: String, phone: String, age: Int, password: String) {
        val request = RegisterRequest(nom, email, age, password, phone)

        viewModelScope.launch {
            try {
                val response = repository.register(request)
                signUpResponse.postValue(response)
            } catch (e: Exception) {

            }
        }
    }



    val loginResponse = MutableLiveData<RegisterResponse?>() // reuse same response model

    fun login(email: String, password: String){
        viewModelScope.launch {
            try {
                val response = repository.login(LoginRequest(email, password))
                loginResponse.postValue(response)
            } catch (e: Exception){

            }
        }
    }

}
