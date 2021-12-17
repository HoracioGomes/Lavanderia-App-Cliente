package com.example.lavanderia_cliente.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.repository.Resource

class PecaRoupaViewModel(private val repository: RepositoryPecaRoupa) : ViewModel() {

    fun buscaTodos(): LiveData<Resource<List<PecaRoupa>?>> {
        return repository.buscaPecasRoupa()
    }

    fun salva(pecaRopa: PecaRoupa): LiveData<Resource<Long?>> {
        return repository.salvaPecaRoupa(pecaRopa)
    }

    fun edita(pecaRoupa: PecaRoupa): LiveData<Resource<PecaRoupa?>> {
        return repository.editaPecaRoupa(pecaRoupa)
    }

    fun deleta(id: Long): LiveData<Resource<Void?>> {
        return repository.deletaPecaRoupa(id)
    }

    fun trocaPosicoes(pecasParaTroca: MutableList<PecaRoupa>?): LiveData<Resource<Void?>> {
        return repository.trocaPosicao(pecasParaTroca)
    }

}