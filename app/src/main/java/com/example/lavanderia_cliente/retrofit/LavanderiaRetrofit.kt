package com.example.lavanderia_cliente.retrofit

import com.example.lavanderia_cliente.retrofit.service.PecaRoupaService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LavanderiaRetrofit {
    private val retrofit: Retrofit

    constructor() {

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.64:3333/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    }

    fun pecaRoupaService(): PecaRoupaService {
        return retrofit.create(PecaRoupaService::class.java)
    }

}