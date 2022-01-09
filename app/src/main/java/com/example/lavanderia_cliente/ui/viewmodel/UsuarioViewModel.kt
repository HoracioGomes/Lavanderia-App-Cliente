package com.example.lavanderia_cliente.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.repository.Resource
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse

class UsuarioViewModel(private val repository: RepositoryUsuario) : ViewModel() {

    val loginAutomaticoLiveData = MutableLiveData<Resource<LoginResponse?>>()
    val loginManualLiveData = MutableLiveData<Resource<LoginResponse?>>()
    val deletaTokenLiveData = MutableLiveData<Resource<Int?>>()

    fun loginAutomatico() {
        repository.loginAutomatico(response = { responseLoginAutomatico ->
            loginAutomaticoLiveData.value = responseLoginAutomatico
        })
    }

    fun logar(email: String, senha: String) {
        repository.logar(email, senha, response = { responseLoginManual ->
            loginManualLiveData.value = responseLoginManual
        })
    }

    fun deletarToken(token: Token) {
        return repository.deletaToken(token, response = { response ->
            deletaTokenLiveData.value = response
        })
    }

}