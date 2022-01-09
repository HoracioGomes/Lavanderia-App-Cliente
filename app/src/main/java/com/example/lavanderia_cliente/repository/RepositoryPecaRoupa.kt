package com.example.lavanderia_cliente.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.lavanderia_cliente.asynctasks.BaseAsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.retrofit.webclient.PecaRoupaWebClient
import com.example.lavanderia_cliente.utils.ProgressBarUtils
import java.util.*
import java.util.logging.Handler
import kotlin.concurrent.schedule

class RepositoryPecaRoupa(
    private val dao: PecaRoupaDao,
) {

    private val pecaRoupaWebClient: PecaRoupaWebClient by lazy {
        PecaRoupaWebClient()
    }

    private val mediadorBuscaPecasRoupa = MediatorLiveData<Resource<List<PecaRoupa>?>?>()
    private val mediadorSalvaPecaRoupa = MediatorLiveData<Resource<Long?>>()
    private val mediadorEditaPecaRoupa = MediatorLiveData<Resource<PecaRoupa?>>()
    private val mediadorDeletaPecaRoupa = MediatorLiveData<Resource<Int?>>()
    private val mediadorTrocaPosicaoPecaRoupa = MediatorLiveData<Resource<Int?>>()

    fun buscaPecasRoupa(): LiveData<Resource<List<PecaRoupa>?>?> {

        var resourceAtualizado = MutableLiveData<Resource<List<PecaRoupa>?>?>()
        mediadorBuscaPecasRoupa.addSource(resourceAtualizado) { resource ->
            mediadorBuscaPecasRoupa.value = resource
        }

        pecaRoupaWebClient.buscaTodasPecas(quandoSucesso = { listaVindaApi ->
            listaVindaApi?.let { listaDaApi ->
                salvaInterno(listaDaApi)

            }
        },
            quandoFalha = {
                val resourceAntigo: Resource<List<PecaRoupa>?>? =
                    mediadorBuscaPecasRoupa.value

                resourceAtualizado.value =
                    criaResourceDeFalha(resourceAntigo = resourceAntigo, mensagem = it)
            }
        )
        return mediadorBuscaPecasRoupa
    }

    private fun salvaInterno(listaNaoNula: List<PecaRoupa>) {

        BaseAsyncTask(enquantoExecuta = {
            dao.salvaVarias(listaNaoNula)
        }, executado = {
            IniciaObeserverMediadorBuscaPecasRoupa()

        }).execute()
    }

    private fun IniciaObeserverMediadorBuscaPecasRoupa() {
        mediadorBuscaPecasRoupa.addSource(buscaInterna()) { listaRetornada ->
            mediadorBuscaPecasRoupa.value = Resource(listaRetornada)
        }
    }

    fun buscaInterna(): LiveData<List<PecaRoupa>?> {
        return dao.todas()
    }


    fun salvaPecaRoupa(
        pecaRoupa: PecaRoupa
    ): LiveData<Resource<Long?>> {

        val resourcePosSalvamento = MutableLiveData<Resource<Long?>>()
        mediadorSalvaPecaRoupa.addSource(resourcePosSalvamento) {
            mediadorSalvaPecaRoupa.value = it
        }

        pecaRoupaWebClient.salvaPeca(pecaRoupa, quandoSucesso = { pecaDaApiPosSalva ->

            BaseAsyncTask(enquantoExecuta = {
                pecaRoupa.id = pecaDaApiPosSalva?.id ?: 0
                dao.salva(pecaRoupa)
            }, executado = { quantidadePecasSalvasInternamente ->
                resourcePosSalvamento.value = Resource(dados = quantidadePecasSalvasInternamente)
            }).execute()

        }, quandoFalha = { erro ->
            resourcePosSalvamento.value = criaResourceDeFalha(null, erro)
        })

        return mediadorSalvaPecaRoupa
    }

    fun editaPecaRoupa(
        pecaRoupa: PecaRoupa?
    ): LiveData<Resource<PecaRoupa?>> {
        val resourcePosSalvamento = MutableLiveData<Resource<PecaRoupa?>>()
        mediadorEditaPecaRoupa.addSource(resourcePosSalvamento) {
            mediadorEditaPecaRoupa.value = it
        }

        pecaRoupaWebClient.editaPeca(pecaRoupa, quandoSucesso = { pecaRetornadaApi ->

            BaseAsyncTask(enquantoExecuta = {
//                Timer().schedule(5000) {
//                    Log.d("TESTE", "ENTROU NO SCHEDULE")
//                }
                dao.edita(pecaRoupa)
            }, executado = {
                if (it > 0) {
                    resourcePosSalvamento.value = Resource(dados = pecaRetornadaApi)
                }
            }).execute()

        }, quandoFalha = { erro ->
            resourcePosSalvamento.value = criaResourceDeFalha(null, erro)
        })

        return mediadorEditaPecaRoupa
    }

    fun trocaPosicao(
        pecasRoupastrocadas: MutableList<PecaRoupa>?
    ): LiveData<Resource<Int?>> {

        val resourcePosEdicao = MutableLiveData<Resource<Int?>>()
        mediadorTrocaPosicaoPecaRoupa.addSource(resourcePosEdicao) {
            mediadorTrocaPosicaoPecaRoupa.value = it
        }

        pecaRoupaWebClient.trocaposicoesPecas(pecasRoupastrocadas, quandoSucesso = {
            trocaPosicaoInterna(pecasRoupastrocadas)

        },
            quandoFalha = {
                resourcePosEdicao.value = criaResourceDeFalha(resourceAntigo = null, mensagem = it)
            })

        return mediadorTrocaPosicaoPecaRoupa

    }

    private fun trocaPosicaoInterna(pecasRoupastrocadas: MutableList<PecaRoupa>?) {
        BaseAsyncTask(enquantoExecuta = {
            dao.edita(pecasRoupastrocadas?.get(0), pecasRoupastrocadas?.get(1))

        }, executado = {
            if (it > 0) {
                mediadorTrocaPosicaoPecaRoupa.value = Resource(it)
            }

        }).execute()
    }

    fun deletaPecaRoupa(id: Long?): LiveData<Resource<Int?>> {

        val resourcePosDelecao = MutableLiveData<Resource<Int?>>()
        mediadorDeletaPecaRoupa.addSource(resourcePosDelecao){
            mediadorDeletaPecaRoupa.value = it
        }

        pecaRoupaWebClient.deletaPecaRoupa(id, quandoSucesso = {
            deletaInterno(id)
        }, quandoFalha = {
            resourcePosDelecao.value = criaResourceDeFalha(resourceAntigo = null, mensagem = it)
        })

        return mediadorDeletaPecaRoupa
    }

    private fun deletaInterno(
        id: Long?
    ) {
        BaseAsyncTask(enquantoExecuta = {
            dao.deletePorId(id)
        }, executado = {
            if(it > 0){
                mediadorDeletaPecaRoupa.value = Resource(it)
            }
        }).execute()
    }


//    interface CallBackRepositorypecaRoupa<T> {
//        fun quandoSucesso(dados: T)
//        fun quandoFalha(erro: String)
//    }
//
//    interface CallBackRepositorypecaRoupaSemBody {
//        fun quandoSucesso()
//        fun quandoFalha(erro: String)
//    }
}