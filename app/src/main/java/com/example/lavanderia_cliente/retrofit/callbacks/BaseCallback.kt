package com.example.lavanderia_cliente.retrofit.callbacks

import android.content.Context
import android.util.Log
import com.example.lavanderia_cliente.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

class BaseCallback<T>(val callbackResposta: CallbackResposta<T>) :
    Callback<T> {
    @EverythingIsNonNull
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val dados: T? = response.body()
            callbackResposta.quandoSucesso(dados)
        } else if (response.code() == 400) {
            callbackResposta.quandoFalha("400")
        } else if (response.code() == 401) {
            callbackResposta.quandoFalha("401")

        } else if (response.code() == 403) {
            callbackResposta.quandoFalha("403")

        } else if (response.code() == 500) {
            callbackResposta.quandoFalha("503")

        }
    }

    @EverythingIsNonNull
    override fun onFailure(call: Call<T>, t: Throwable) {
        callbackResposta.quandoFalha("falha" + t.message)
    }

    interface CallbackResposta<T> {
        fun quandoSucesso(dados: T?)
        fun quandoFalha(mensagem: String)
    }
}