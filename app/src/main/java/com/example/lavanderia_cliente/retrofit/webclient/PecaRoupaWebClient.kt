package com.example.lavanderia_cliente.retrofit.webclient

import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.retrofit.LavanderiaRetrofit
import com.example.lavanderia_cliente.retrofit.service.PecaRoupaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUISICAO_NAO_SUCEDIDA = "Requisição não sucedida"
private const val REALIZE_NOVO_LOGIN = "Autorização inválida!\nRealize um novo login."

class PecaRoupaWebClient(private val pecaRoupaService: PecaRoupaService = LavanderiaRetrofit().pecaRoupaService) {


    private fun <T> executaRequisicao(
        call: Call<T>,
        quandoSucesso: (dados: T?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {

        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    quandoSucesso(response.body())
                } else if(response.code() == 403) {
                    quandoFalha(REALIZE_NOVO_LOGIN)
                }
                else {
                    quandoFalha(REQUISICAO_NAO_SUCEDIDA)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                quandoFalha(t.message)
            }

        })

    }

    fun buscaTodasPecas(
        quandoSucesso: (listaPecas: List<PecaRoupa>?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            pecaRoupaService.buscaTodasPecas(),
            quandoSucesso = quandoSucesso,
            quandoFalha = quandoFalha
        )

    }

    fun salvaPeca(
        pecaRoupa: PecaRoupa,
        quandoSucesso: (pecaRoupa: PecaRoupa?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(pecaRoupaService.salva(pecaRoupa), quandoSucesso, quandoFalha)
    }

    fun editaPeca(
        pecaRoupa: PecaRoupa,
        quandoSucesso: (pecaRoupa: PecaRoupa?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(pecaRoupaService.edita(pecaRoupa), quandoSucesso, quandoFalha)
    }

    fun trocaposicoesPecas(
        pecasRoupas: MutableList<PecaRoupa>?,
        quandoSucesso: (void: Void?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(pecaRoupaService.trocaPosicao(pecasRoupas), quandoSucesso, quandoFalha)
    }

    fun deletaPecaRoupa(
        id: Long?,
        quandoSucesso: (void: Void?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(pecaRoupaService.delete(id), quandoSucesso, quandoFalha)
    }

}