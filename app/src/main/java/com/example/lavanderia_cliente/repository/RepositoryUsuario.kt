package com.example.lavanderia_cliente.repository

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lavanderia_cliente.asynctasks.BaseAsyncTask
import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.retrofit.LavanderiaRetrofit
import com.example.lavanderia_cliente.retrofit.callbacks.BaseCallback
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse
import com.example.lavanderia_cliente.retrofit.webclient.UsuarioWebClient


class RepositoryUsuario(
    val clienteDao: ClienteDao,
    val tokenDao: TokenDao,
    val usuarioWebClient: UsuarioWebClient = UsuarioWebClient()
) {

    fun logar(
        email: String,
        password: String
    ): LiveData<Resource<LoginResponse?>> {

        val resourceLogin = MutableLiveData<Resource<LoginResponse?>>()

        val dadosLogin = HashMap<String, String>()
        dadosLogin["email"] = email
        dadosLogin["password"] = password

        usuarioWebClient.logar(dadosLogin, quandoSucesso = { loginResponse ->

            if (loginResponse != null) {
                var cliente = loginResponse.cliente
                var token = loginResponse.token

                if (cliente != null && token != null) {

                    BaseAsyncTask(enquantoExecuta = {
                        clienteDao.salva(cliente)
                        tokenDao.salva(token)

                    },
                        executado = {

                            resourceLogin.value = Resource(loginResponse)

                        }
                    ).execute()
                }
            }


        },
            quandoFalha = {
                resourceLogin.value = criaResourceDeFalha(resourceAntigo = null, it)
            }
        )

        return resourceLogin

    }

    fun loginAutomatico(): LiveData<Resource<LoginResponse?>> {

        val resourceLoginAutomatico = MutableLiveData<Resource<LoginResponse?>>()
        var dadosLogin: LoginResponse? = null

        BaseAsyncTask(enquantoExecuta = {
            val todos = clienteDao.todos()

            if (todos.size == 1) {
                val token = tokenDao.buscaToken(todos[0].id)
                if (token != null) {
                    dadosLogin = LoginResponse(todos[0], token)
                }
            }
            dadosLogin
        },
            executado = {
                if (dadosLogin != null) {
                    resourceLoginAutomatico.value = Resource(dados = dadosLogin)
                }
            }
        ).execute()

        return resourceLoginAutomatico
    }


    fun deletaToken(token: Token): LiveData<Resource<Int?>> {
        val resourcePosDelecao = MutableLiveData<Resource<Int?>>()
        BaseAsyncTask<Int>(enquantoExecuta = {
            val posDelecao = tokenDao.delete(token)
            posDelecao
        }, executado = {
            resourcePosDelecao.value = Resource(dados = it)
        }).execute()

        return resourcePosDelecao

    }

}