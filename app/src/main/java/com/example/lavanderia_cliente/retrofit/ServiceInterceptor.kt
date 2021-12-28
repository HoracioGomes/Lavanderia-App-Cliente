package com.example.lavanderia_cliente.retrofit


import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.token
import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.headers("No-Authentication").isEmpty() && request.headers("Authorizathion")
                .isEmpty()
        ) {
            if (!token?.token.isNullOrEmpty()) {
                val finaToken = "Bearer ${token?.token}"
                request = request.newBuilder()
                    .addHeader("Authorization", finaToken)
                    .build()
            }
        }
        return chain.proceed(request)

    }
}