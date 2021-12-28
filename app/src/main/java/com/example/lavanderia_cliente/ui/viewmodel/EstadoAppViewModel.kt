package com.example.lavanderia_cliente.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EstadoAppViewModel : ViewModel() {

    val componentesVisuais: LiveData<ComponetesVisuais> get() = _componentesVisuais

    private var _componentesVisuais: MutableLiveData<ComponetesVisuais> =
        MutableLiveData<ComponetesVisuais>().also { it.value = temComponentes }

    var temComponentes: ComponetesVisuais = ComponetesVisuais()
        set(value) {
            field = value
            _componentesVisuais.value = field
        }
}

class ComponetesVisuais(
    val appBar: Boolean = false,
    val bottomNavigation: Boolean = false
)