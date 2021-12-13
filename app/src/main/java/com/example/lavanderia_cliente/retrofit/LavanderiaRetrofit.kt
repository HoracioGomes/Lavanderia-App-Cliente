package com.example.lavanderia_cliente.retrofit

import com.example.lavanderia_cliente.retrofit.service.LoginService
import com.example.lavanderia_cliente.retrofit.service.PecaRoupaService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class LavanderiaRetrofit {
    private var retrofit: Retrofit? = null

    companion object {
        var baseUrl = "url_here"
    }

    constructor() {

        if (retrofit == null) {
            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient.Builder()
//                .readTimeout(45,TimeUnit.SECONDS)
//                .writeTimeout(45, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(ServiceInterceptor())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        }

    }

    fun pecaRoupaService(): PecaRoupaService? {
        return retrofit?.create(PecaRoupaService::class.java)
    }

    fun loginService(): LoginService? {
        return retrofit?.create(LoginService::class.java)
    }

}