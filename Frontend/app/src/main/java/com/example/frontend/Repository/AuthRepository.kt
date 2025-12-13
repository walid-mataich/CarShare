package com.example.frontend.Repository

import android.content.Context
import com.example.frontend.api.RetrofitInstance
import com.example.frontend.model.LoginRequest
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.RegisterResponse

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

        if(token != null && name != null && email != null) {
            saveToken(token, name, email)

        }
        return res;
    }

    private fun saveToken(token: String, name: String, email: String) {
        val editor = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)?.edit()
        editor?.putString("TOKEN", token)
        editor?.putString("USER_EMAIL", email)
        editor?.putString("USER_NAME", name)
        editor?.apply()
    }


    fun getToken(): String? {
        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("TOKEN", null)
    }


}
