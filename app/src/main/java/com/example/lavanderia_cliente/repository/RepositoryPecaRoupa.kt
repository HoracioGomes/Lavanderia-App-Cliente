package com.example.lavanderia_cliente.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.lavanderia_cliente.asynctasks.BaseAsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.retrofit.webclient.PecaRoupaWebClient

class RepositoryPecaRoupa(
    private val dao: PecaRoupaDao,
) {

    private val pecaRoupaWebClient: PecaRoupaWebClient by lazy {
        PecaRoupaWebClient()
    }

    private val mediadorBuscaPecasRoupa = MediatorLiveData<Resource<List<PecaRoupa>?>>()
    private val mediadorSalvaPecaRoupa = MediatorLiveData<Resource<Long?>>()
    private val mediadorEditaPecaRoupa = MediatorLiveData<Resource<PecaRoupa?>>()

    fun buscaPecasRoupa(): LiveData<Resource<List<PecaRoupa>?>> {


        mediadorBuscaPecasRoupa.addSource(buscaInterna()) { listaRetornada ->
            mediadorBuscaPecasRoupa.value = Resource(dados = listaRetornada)
        }

        var resourceAtualizado = MutableLiveData<Resource<List<PecaRoupa>?>>()
        mediadorBuscaPecasRoupa.addSource(resourceAtualizado) { resource ->
            mediadorBuscaPecasRoupa.value = resource
        }


        pecaRoupaWebClient.buscaTodasPecas(quandoSucesso = { listaVindaApi ->

            listaVindaApi?.let { listaNaoNula -> salvaInterno(listaNaoNula) }

        },
            quandoFalha = {
                val resourceAntigo: Resource<List<PecaRoupa>?>? =
                    mediadorBuscaPecasRoupa.value

                val resourceCriado: Resource<List<PecaRoupa>?> = if (resourceAntigo != null) {
                    criaResourceDeFalha(resourceAntigo = resourceAntigo, mensagem = it)

                } else {
                    criaResourceDeFalha(resourceAntigo = null, mensagem = it)

                }
                resourceAtualizado.value = resourceCriado

            }
        )

        return mediadorBuscaPecasRoupa
    }

    private fun salvaInterno(listaNaoNula: List<PecaRoupa>) {

        BaseAsyncTask(enquantoExecuta = {
            dao.salvaVarias(listaNaoNula)
        }, executado = {

        }).execute()
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
                dao.edita(pecaRoupa)
            }, executado = {
                resourcePosSalvamento.value = Resource(dados = pecaRetornadaApi)

            }).execute()

        }, quandoFalha = { erro ->
            resourcePosSalvamento.value = criaResourceDeFalha(null, erro)
        })

        return mediadorEditaPecaRoupa
    }

    fun trocaPosicao(
        pecasRoupastrocadas: MutableList<PecaRoupa>?
    ): LiveData<Resource<Void?>> {

        val resourcePosEdicao = MutableLiveData<Resource<Void?>>()

        pecaRoupaWebClient.trocaposicoesPecas(pecasRoupastrocadas, quandoSucesso = {
            trocaPosicaoInterna(pecasRoupastrocadas)

        },
            quandoFalha = {
                resourcePosEdicao.value = criaResourceDeFalha(resourceAntigo = null, mensagem = it)
            })

        return resourcePosEdicao

    }

    private fun trocaPosicaoInterna(pecasRoupastrocadas: MutableList<PecaRoupa>?) {
        BaseAsyncTask(enquantoExecuta = {
            dao.edita(pecasRoupastrocadas?.get(0), pecasRoupastrocadas?.get(1))
        }, executado = {

        }).execute()
    }

    fun deletaPecaRoupa(id: Long?): LiveData<Resource<Void?>> {

        val resourcePosDelecao = MutableLiveData<Resource<Void?>>()

        pecaRoupaWebClient.deletaPecaRoupa(id, quandoSucesso = {
            deletaInterno(id, resourcePosDelecao)
        }, quandoFalha = {
            resourcePosDelecao.value = criaResourceDeFalha(resourceAntigo = null, mensagem = it)
        })

        return resourcePosDelecao
    }

    private fun deletaInterno(
        id: Long?,
        resourcePosDelecao: MutableLiveData<Resource<Void?>>
    ) {
        BaseAsyncTask(enquantoExecuta = {
            dao.deletePorId(id)
        }, executado = {
            resourcePosDelecao.value = Resource(dados = null)
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