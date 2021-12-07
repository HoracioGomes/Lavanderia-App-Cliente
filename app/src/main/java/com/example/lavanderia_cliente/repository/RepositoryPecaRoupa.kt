package com.example.lavanderia_cliente.repository

import android.content.Context
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.retrofit.LavanderiaRetrofit
import com.example.lavanderia_cliente.retrofit.callbacks.BaseCallBackSemBody
import com.example.lavanderia_cliente.retrofit.callbacks.BaseCallback

class RepositoryPecaRoupa(val context: Context) {
    val pecaRoupaDao = LavanderiaDatabase.getAppDatabase(context).getPecaRoupaDao()
    val pecaRoupaService = LavanderiaRetrofit().pecaRoupaService()

    fun buscaPecasRoupa(callback: CallBackRepositorypecaRoupa<MutableList<PecaRoupa>>) {

// Busca interna
//    BuscaPecasRoupaTask(pecaRoupaDao, object : BuscaPecasRoupaTask.ListenerBuscaPecasRoupa {
//            override fun retorno(pecasRoupasRetornadas: MutableList<PecaRoupa>?) {
//                if (pecasRoupasRetornadas != null) {
//                    callback.quandoSucesso(pecasRoupasRetornadas)
//                } else {
//                    callback.quandoFalha(context.getString(R.string.mensagem_sem_dados_internos))
//                }
//
//            }
//        }).execute()

        val callPecaRoupa = pecaRoupaService.buscaTodasPecas()

        callPecaRoupa.enqueue(
            BaseCallback(context,
                object : BaseCallback.CallbackResposta<MutableList<PecaRoupa>> {
                    override fun quandoSucesso(dados: MutableList<PecaRoupa>?) {
                        dados?.let { callback.quandoSucesso(it) }
                    }

                    override fun quandoFalha(mensagem: String) {
                        callback.quandoFalha(mensagem)
                    }

                })
        )

    }

    fun solicitaPecaRoupaDelivery(
        pecaRoupa: PecaRoupa,
        callback: CallBackRepositorypecaRoupa<PecaRoupa>
    ) {

// Salvamento interno
//        SalvaPecaRoupaTask(
//            dao,
//            pecaRoupa,
//            object : SalvaPecaRoupaTask.ListenerSalvaPeca {
//                override fun salvo(id: Long?) {
//                    if (id != null) {
//                        pecaRoupa.id = id
//                        pecaRoupa.posicaoNaLista = id
//                        EditaPecaRoupaTask(
//                            dao,
//                            mutableListOf(pecaRoupa),
//                            object : EditaPecaRoupaTask.ListenerEditaPecaRoupa {
//                                override fun editado(nomePeca: String?) {
//                                }
//
//                            }).execute()
//                    }
//                }
//
//            }).execute()

        val callSalvaPecaRoupa = pecaRoupaService.salva(pecaRoupa)
        callSalvaPecaRoupa.enqueue(
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
        val callEditaPecaRoupa = pecaRoupaService.edita(pecasRoupa)
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
        val call = pecaRoupaService.trocaPosicao(pecasRoupastrocadas)
        call.enqueue(
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

// Deletar internamente
//        DeletaPecaRoupaTask(pecaRoupaDao, pecasRoupas?.get(position),
//            object : DeletaPecaRoupaTask.ListenerDeletaPecaRoupa {
//                override fun deletada(nomePeca: String?) {
//                }
//
//            }).execute()

        val call = pecaRoupaService.delete(id)
        call.enqueue(
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


    interface CallBackRepositorypecaRoupa<T> {
        fun quandoSucesso(dados: T)
        fun quandoFalha(erro: String)
    }

    interface CallBackRepositorypecaRoupaSemBody {
        fun quandoSucesso()
        fun quandoFalha(erro: String)
    }
}