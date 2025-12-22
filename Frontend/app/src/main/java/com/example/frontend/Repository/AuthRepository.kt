package com.example.frontend.Repository

import android.content.Context
import android.util.Log
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.LoginRequest
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository() {

    private var context: Context? = null

    fun setContext(ctx: Context) {
        context = ctx
    }


    suspend fun register(request: RegisterRequest): RegisterResponse {
        return RetrofitInstance.apiInterface.register(request)
    }

    suspend fun login(request: LoginRequest): RegisterResponse {
        val res = RetrofitInstance.apiInterface.login(request);

        val token = res.token
        val name = res.nom
        val email = res.email
        val userId = res.userId

        if(token != null && name != null && email != null) {
            saveToken(token, name, email, userId)

        }
        return res;
    }


    suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = getToken()
                if (token != null) {
                    val response = RetrofitInstance.apiInterface.logout()
                    return@withContext response.isSuccessful
                }
                false
            } catch (e: Exception) {
                Log.e("AuthRepository", "Logout failed", e)
                false
            }
        }
    }

    fun clearToken() {
        val editor = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)?.edit()
        editor?.remove("TOKEN")
        editor?.remove("USER_EMAIL")
        editor?.remove("USER_NAME")
        editor?.remove("USER_ID")
        editor?.apply()
    }

    private fun saveToken(token: String, name: String, email: String,userId: Long) {
        val editor = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)?.edit()
        editor?.putString("TOKEN", token)
        editor?.putString("USER_EMAIL", email)
        editor?.putString("USER_NAME", name)
        editor?.putLong("USER_ID", userId)

        editor?.apply()
    }





    fun getToken(): String? {
        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("TOKEN", null)
    }


}
