package com.example.lavanderia_cliente.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.retrofit.webclient.PecaRoupaWebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        mediadorBuscaPecasRoupa.addSource(buscaInterna()) { listaRetornada ->
            mediadorBuscaPecasRoupa.value = Resource(listaRetornada)
        }

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
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                dao.salvaVarias(listaNaoNula)
            }
        }.start()

    }


    private fun buscaInterna(): LiveData<List<PecaRoupa>?> {
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

            CoroutineScope(Dispatchers.Main).launch {
                var idPecaSalva: Long
                withContext(Dispatchers.Default) {
                    pecaRoupa.id = pecaDaApiPosSalva?.id ?: 0
                    idPecaSalva = dao.salva(pecaRoupa)
                }
                resourcePosSalvamento.value = Resource(dados = idPecaSalva)
            }.start()

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

            CoroutineScope(Dispatchers.Main).launch {
                var itensEditados: Int
                withContext(Dispatchers.Default) {
                    itensEditados = dao.edita(pecaRoupa)
                }
                if (itensEditados > 0) {
                    resourcePosSalvamento.value = Resource(dados = pecaRetornadaApi)
                }
            }.start()

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

        CoroutineScope(Dispatchers.Main).launch {
            var linhasEditadas: Int
            withContext(Dispatchers.Default) {
                linhasEditadas = dao.edita(pecasRoupastrocadas?.get(0), pecasRoupastrocadas?.get(1))
            }
            if (linhasEditadas > 1) {
                mediadorTrocaPosicaoPecaRoupa.value = Resource(linhasEditadas)
            }
        }.start()

    }

    fun deletaPecaRoupa(id: Long?): LiveData<Resource<Int?>> {

        val resourcePosDelecao = MutableLiveData<Resource<Int?>>()
        mediadorDeletaPecaRoupa.addSource(resourcePosDelecao) {
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

        CoroutineScope(Dispatchers.Main).launch {
            var qtdItensDeletados: Int
            withContext(Dispatchers.Default) {
                qtdItensDeletados = dao.deletePorId(id)
            }
            if (qtdItensDeletados > 0) {
                mediadorDeletaPecaRoupa.value = Resource(qtdItensDeletados)
            }
        }.start()
    }

}