package com.example.lavanderia_cliente.retrofit.callbacks

import android.content.Context
import com.example.lavanderia_cliente.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

class BaseCallback<T>(val context: Context, val callbackResposta: CallbackResposta<T>) :
    Callback<T> {
    @EverythingIsNonNull
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val dados: T? = response.body()
            callbackResposta.quandoSucesso(dados)
        } else {
            callbackResposta.quandoFalha(context.getString(R.string.mensagem_impossibilidade_requisicao))
        }
    }

    @EverythingIsNonNull
    override fun onFailure(call: Call<T>, t: Throwable) {
        callbackResposta.quandoFalha(context.getString(R.string.mensagem_falha_requisicao)+t.message)
    }

    interface CallbackResposta<T> {
        fun quandoSucesso(dados: T?)
        fun quandoFalha(mensagem: String)
    }
}