package com.example.lavanderia_cliente.retrofit.webclient

import com.example.lavanderia_cliente.retrofit.LavanderiaRetrofit
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse
import com.example.lavanderia_cliente.retrofit.service.UsuarioService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val FALHA_NA_REQUISICAO = "Houve uma falha na requisição!"
private const val DADOS_LOGIN_INVALIDOS = "Os dados não conferem!"

class UsuarioWebClient(
    val usuarioService: UsuarioService = LavanderiaRetrofit().usuarioService
) {
    private fun <T> executaRequisicao(
        call: Call<T>,
        quandoSucesso: (dados: T?) -> Unit,
        quandoFalha: (mensagem: String?) -> Unit
    ) {

        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    quandoSucesso(response.body())
                } else if(response.code() == 401) {
                    quandoFalha(DADOS_LOGIN_INVALIDOS)
                }
                else {
                    quandoFalha(FALHA_NA_REQUISICAO)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                quandoFalha(FALHA_NA_REQUISICAO + " " + t.message)
            }

        })

    }

    fun logar(
        dadosLogin: HashMap<String, String>,
        quandoSucesso: (dadosRetornados: LoginResponse?) -> Unit,
        quandoFalha: (mensagem: String?) -> Unit
    ) {

        executaRequisicao(
            usuarioService.auth(dadosLogin),
            quandoSucesso = quandoSucesso,
            quandoFalha = quandoFalha
        )

    }

    fun verificarToken(
        token: String,
        quandoSucesso: (resposta: Boolean?) -> Unit,
        quandoFalha: (mensagem: String?) -> Unit
    ) {
        executaRequisicao(
            usuarioService.verifyToken(token),
            quandoSucesso = quandoSucesso,
            quandoFalha = quandoFalha
        )
    }

}