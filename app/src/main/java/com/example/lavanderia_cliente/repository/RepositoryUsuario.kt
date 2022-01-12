package com.example.lavanderia_cliente.repository

import com.example.lavanderia_cliente.asynctasks.BaseAsyncTask
import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse
import com.example.lavanderia_cliente.retrofit.webclient.UsuarioWebClient


class RepositoryUsuario(
    val clienteDao: ClienteDao,
    val tokenDao: TokenDao,
) {

    val usuarioWebClient: UsuarioWebClient by lazy {
        UsuarioWebClient()
    }

    fun logar(
        email: String,
        password: String,
        response: (resource: Resource<LoginResponse?>?) -> Unit
    ) {

        var resourceLogin: Resource<LoginResponse?>? = null

        val dadosLogin = HashMap<String, String>()
        dadosLogin["email"] = email
        dadosLogin["password"] = password

        usuarioWebClient.logar(dadosLogin, quandoSucesso = { loginResponse ->

            if (loginResponse != null) {
                val cliente = loginResponse.cliente
                val token = loginResponse.token

                if (cliente != null && token != null) {

                    BaseAsyncTask(enquantoExecuta = {
                        clienteDao.salva(cliente)
                        tokenDao.salva(token)

                    },
                        executado = {

                            resourceLogin = Resource(loginResponse)
                            response(resourceLogin)

                        }
                    ).execute()
                }
            }


        },
            quandoFalha = {
                resourceLogin = criaResourceDeFalha(resourceAntigo = null, it)
                response(resourceLogin)

            }
        )


    }

    fun loginAutomatico(response: (resource: Resource<LoginResponse?>?) -> Unit) {
        var dadosLogin: Resource<LoginResponse?>? = null

        BaseAsyncTask(enquantoExecuta = {
            val todos = clienteDao.todos()

            if (todos.size == 1) {
                val token = tokenDao.buscaToken(todos[0].id)
                if (token != null) {
                    dadosLogin = Resource(dados = LoginResponse(todos[0], token))
                }
            }
            dadosLogin
        },
            executado = {
                response(dadosLogin)
            }
        ).execute()

    }


    fun deletaToken(token: Token, response: (Resource<Int?>?) -> Unit) {
        var resourcePosDelecao: Resource<Int?>? = null
        BaseAsyncTask<Int>(enquantoExecuta = {
            val posDelecao = tokenDao.delete(token)
            posDelecao
        }, executado = {
            resourcePosDelecao = Resource(dados = it)
            response(resourcePosDelecao)
        }).execute()

    }

}