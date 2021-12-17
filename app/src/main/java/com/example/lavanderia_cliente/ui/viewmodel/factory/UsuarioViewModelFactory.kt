package com.example.lavanderia_cliente.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.ui.viewmodel.UsuarioViewModel

class UsuarioViewModelFactory(val repository: RepositoryUsuario): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsuarioViewModel(repository) as T
    }
}