package com.example.frontend.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
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


    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> = _logoutResult

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

    // New logout method
    fun logout(context: Context) {
        repository.setContext(context)
        viewModelScope.launch {
            try {
                // First, call backend logout API
                val success = repository.logout()
                // Then clear local token regardless of backend result
                repository.clearToken()
                _logoutResult.postValue(success)
            } catch (e: Exception) {
                // Even if API call fails, clear local token
                repository.clearToken()
                _logoutResult.postValue(false)
            }
        }
    }






}
