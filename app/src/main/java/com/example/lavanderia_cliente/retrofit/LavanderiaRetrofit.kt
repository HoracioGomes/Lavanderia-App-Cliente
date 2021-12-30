package com.example.lavanderia_cliente.retrofit

import com.example.lavanderia_cliente.Url
import com.example.lavanderia_cliente.retrofit.service.UsuarioService
import com.example.lavanderia_cliente.retrofit.service.PecaRoupaService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LavanderiaRetrofit {

    companion object {
        // The Url class is not shared. put your own ip.
        // Ex: "http://xxx.xx.xxx.xx:3333(3333 is the Lavanderia_Api port)"
        var baseUrl = Url.myUrl
    }


    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    }

    private val client by lazy {
        OkHttpClient.Builder()
//                .readTimeout(45,TimeUnit.SECONDS)
//                .writeTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ServiceInterceptor())
            .build()
    }

    private val gson by lazy {
        GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }


    val pecaRoupaService: PecaRoupaService by lazy {
        retrofit.create(PecaRoupaService::class.java)
    }

    val usuarioService: UsuarioService by lazy {
        retrofit.create(UsuarioService::class.java)
    }

}