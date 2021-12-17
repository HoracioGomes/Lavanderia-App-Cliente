package com.example.lavanderia_cliente.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.repository.Resource
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse

class UsuarioViewModel(private val repository: RepositoryUsuario) : ViewModel() {

 fun logar(email: String, senha: String): LiveData<Resource<LoginResponse?>>{
     return repository.logar(email, senha)
 }

fun loginAutomatico(): LiveData<Resource<LoginResponse?>>{
    return repository.loginAutomatico()
}

fun deletarToken(token: Token): LiveData<Resource<Int?>>{
    return repository.deletaToken(token)
}

}