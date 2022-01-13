package com.example.lavanderia_cliente.repository

import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse
import com.example.lavanderia_cliente.retrofit.webclient.UsuarioWebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Default) {
                            clienteDao.salva(cliente)
                            tokenDao.salva(token)
                        }
                        resourceLogin = Resource(loginResponse)
                        response(resourceLogin)
                    }.start()

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
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val todos = clienteDao.todos()
                if (todos.size == 1) {
                    val token = tokenDao.buscaToken(todos[0].id)
                    if (token != null) {
                        dadosLogin = Resource(dados = LoginResponse(todos[0], token))
                    }
                }
            }
            response(dadosLogin)
        }.start()

    }


    fun deletaToken(token: Token, response: (Resource<Int?>?) -> Unit) {
        var resourcePosDelecao: Resource<Int?>? = null

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                resourcePosDelecao = Resource(dados = tokenDao.delete(token))
            }
            response(resourcePosDelecao)
        }.start()

    }

}