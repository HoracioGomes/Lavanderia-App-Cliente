package com.example.lavanderia_cliente.repository

import android.content.Context
import com.example.lavanderia_cliente.asynctasks.BaseAsyncTask
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.retrofit.LavanderiaRetrofit
import com.example.lavanderia_cliente.retrofit.callbacks.BaseCallBackSemBody
import com.example.lavanderia_cliente.retrofit.callbacks.BaseCallback
import com.example.lavanderia_cliente.ui.activity.ListaRoupasActivity.Companion.token

class RepositoryPecaRoupa(val context: Context) {
    val tokenDao = LavanderiaDatabase.getAppDatabase(context).getTokenDao()
    val pecaRoupaService = LavanderiaRetrofit().pecaRoupaService()

    fun buscaPecasRoupa(callback: CallBackRepositorypecaRoupa<MutableList<PecaRoupa>>) {

        val callPecaRoupa = pecaRoupaService?.buscaTodasPecas()
        callPecaRoupa?.enqueue(
            BaseCallback(context,
                object : BaseCallback.CallbackResposta<MutableList<PecaRoupa>> {
                    override fun quandoSucesso(dados: MutableList<PecaRoupa>?) {
                        dados?.let { callback.quandoSucesso(it) }
                    }

                    override fun quandoFalha(mensagem: String) {
                        deletaTokenTask(token, callback, mensagem)
                    }

                })
        )

    }

    fun solicitaPecaRoupaDelivery(
        pecaRoupa: PecaRoupa,
        callback: CallBackRepositorypecaRoupa<PecaRoupa>
    ) {


        val callSalvaPecaRoupa = pecaRoupaService?.salva(pecaRoupa)
        callSalvaPecaRoupa?.enqueue(
            BaseCallback(
                context,
                object : BaseCallback.CallbackResposta<PecaRoupa> {
                    override fun quandoSucesso(dados: PecaRoupa?) {
                        dados?.let { callback.quandoSucesso(it) }
                    }

                    override fun quandoFalha(mensagem: String) {
                        quandoFalha(mensagem)

                    }

                })
        )
    }

    fun editaPecaRoupa(
        pecasRoupa: PecaRoupa,
        callback: CallBackRepositorypecaRoupa<PecaRoupa>
    ) {
        val callEditaPecaRoupa = pecaRoupaService?.edita(pecasRoupa)
        callEditaPecaRoupa?.enqueue(
            BaseCallback(
                context,
                object : BaseCallback.CallbackResposta<PecaRoupa?> {
                    override fun quandoSucesso(dados: PecaRoupa?) {
                        dados?.let { callback.quandoSucesso(it) }
                    }

                    override fun quandoFalha(mensagem: String) {
                        quandoFalha(mensagem)
                    }

                })
        )
    }

    fun trocaPosicao(
        pecasRoupastrocadas: MutableList<PecaRoupa?>,
        callback: CallBackRepositorypecaRoupaSemBody
    ) {
        val call = pecaRoupaService?.trocaPosicao(pecasRoupastrocadas)
        call?.enqueue(
            BaseCallBackSemBody(
                context,
                object : BaseCallBackSemBody.CallBackRespostaSemBody {
                    override fun quandoSucesso() {
                        callback.quandoSucesso()
                    }

                    override fun quandoFalha(mensagem: String) {
                        callback.quandoFalha(mensagem)
                    }

                })
        )

    }

    fun deletaPecaRoupa(id: Long?, callback: CallBackRepositorypecaRoupaSemBody) {

        val call = pecaRoupaService?.delete(id)
        call?.enqueue(
            BaseCallBackSemBody(
                context,
                object : BaseCallBackSemBody.CallBackRespostaSemBody {
                    override fun quandoSucesso() {
                        callback.quandoSucesso()
                    }

                    override fun quandoFalha(mensagem: String) {
                        callback.quandoFalha(mensagem)
                    }

                })
        )
    }

    private fun deletaTokenTask(
        token: Token?,
        callbackRepositoryPecaRoupa: CallBackRepositorypecaRoupa<MutableList<PecaRoupa>>,
        mensagem: String
    ) {
        BaseAsyncTask<Int?>(object :
            BaseAsyncTask.ListenerBaseAsyncExecuta<Int?> {
            override fun executando(): Int? {
                return token?.let { tokenDao.delete(it) }
            }

        },
            object :
                BaseAsyncTask.ListenerBaseAsyncFinaliza<Int?> {
                override fun finalizado(result: Int?) {

                    callbackRepositoryPecaRoupa.quandoFalha(
                        mensagem
                    )
                }

            }).execute()
    }


    interface CallBackRepositorypecaRoupa<T> {
        fun quandoSucesso(dados: T)
        fun quandoFalha(erro: String)
    }

    interface CallBackRepositorypecaRoupaSemBody {
        fun quandoSucesso()
        fun quandoFalha(erro: String)
    }
}