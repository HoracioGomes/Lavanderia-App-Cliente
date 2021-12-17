package com.example.lavanderia_cliente.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel

class PecaRoupaViewModelFactory(val repository: RepositoryPecaRoupa): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PecaRoupaViewModel(repository) as T
    }
}