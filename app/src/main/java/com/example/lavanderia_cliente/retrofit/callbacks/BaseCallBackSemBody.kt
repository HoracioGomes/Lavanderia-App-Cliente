package com.example.lavanderia_cliente.retrofit.callbacks

import android.content.Context
import com.example.lavanderia_cliente.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseCallBackSemBody<T>(
    val context: Context,
    val callBackSemBody: CallBackRespostaSemBody
) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {

        if (response.isSuccessful) {
            callBackSemBody.quandoSucesso()
        } else {
            callBackSemBody.quandoFalha(context.getString(R.string.mensagem_impossibilidade_requisicao))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        callBackSemBody.quandoFalha(context.getString(R.string.mensagem_falha_requisicao) + t.message)
    }

    interface CallBackRespostaSemBody {
        fun quandoSucesso()
        fun quandoFalha(mensagem: String)

    }
}