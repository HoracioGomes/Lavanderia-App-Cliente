package com.example.lavanderia_cliente.retrofit.service

import com.example.lavanderia_cliente.retrofit.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @POST("session/authenticate")
    @Headers("No-Authentication: true")
    fun auth(@Body login: HashMap<String, String>):Call<LoginResponse>

    @POST("session/verify-token")
    fun verifyToken(@Header("Authorization") token: String):Call<Boolean>

}