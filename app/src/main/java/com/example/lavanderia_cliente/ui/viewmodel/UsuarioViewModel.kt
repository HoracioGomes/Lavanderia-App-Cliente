package com.example.lavanderia_cliente.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.repository.Resource
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse

class UsuarioViewModel(private val repository: RepositoryUsuario) : ViewModel() {

    val loginAutomaticoLiveData = MutableLiveData<Resource<LoginResponse?>?>()

    fun loginAutomatico() {
        repository.loginAutomatico(response = { responseLoginAutomatico ->
            loginAutomaticoLiveData.value = responseLoginAutomatico
        })

    }

    fun logar(email: String, senha: String): LiveData<Resource<LoginResponse?>> {
        return repository.logar(email, senha)
    }


    fun deletarToken(token: Token): LiveData<Resource<Int?>> {
        return repository.deletaToken(token)
    }

}