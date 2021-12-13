package com.example.lavanderia_cliente.repository

import android.content.Context
import com.example.lavanderia_cliente.asynctasks.BaseAsyncTask
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.retrofit.LavanderiaRetrofit
import com.example.lavanderia_cliente.retrofit.callbacks.BaseCallback
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse


class RepositoryLogin(val context: Context) {
    val loginService = LavanderiaRetrofit().loginService()
    val clienteDao = LavanderiaDatabase.getAppDatabase(context).getClienteDao()
    val tokenDao = LavanderiaDatabase.getAppDatabase(context).getTokenDao()

    fun auth(
        email: String,
        password: String,
        callbackRepositoryLogin: CallbackRepositoryLogin<LoginResponse?>
    ) {

        val dadosLogin = HashMap<String, String>()
        dadosLogin["email"] = email
        dadosLogin["password"] = password

        val call = loginService?.auth(dadosLogin)
        call?.enqueue(BaseCallback(context, object : BaseCallback.CallbackResposta<LoginResponse> {
            override fun quandoSucesso(dados: LoginResponse?) {
                BaseAsyncTask(object : BaseAsyncTask.ListenerBaseAsyncExecuta<LoginResponse?> {
                    override fun executando(): LoginResponse? {
                        var login: LoginResponse? = null
                        if (dados != null) {
                            var cliente = dados.cliente
                            var token = dados.token

                            if (cliente != null && token != null) {
                                clienteDao.salva(cliente)
                                tokenDao.salva(token)
                                login = LoginResponse(cliente = cliente, token = token)
                            }
                        }
                        return login

                    }

                }, object : BaseAsyncTask.ListenerBaseAsyncFinaliza<LoginResponse?> {
                    override fun finalizado(result: LoginResponse?) {
                        if (result?.cliente ?: null != null) {
                            callbackRepositoryLogin.quandoSucesso(result)
                        } else {
                            callbackRepositoryLogin.quandoFalha("Não houve dados retornados")
                        }
                    }

                }).execute()

            }

            override fun quandoFalha(mensagem: String) {
                callbackRepositoryLogin.quandoFalha(mensagem)
            }

        }))


    }

    fun loginAutomatico(
        callbackRepositoryLogin: CallbackRepositoryLogin<LoginResponse?>
    ) {

        BaseAsyncTask(object : BaseAsyncTask.ListenerBaseAsyncExecuta<MutableList<Cliente>> {
            override fun executando(): MutableList<Cliente> {
                val todos = clienteDao.todos()

                return todos
            }

        },

            object : BaseAsyncTask.ListenerBaseAsyncFinaliza<MutableList<Cliente>> {
                override fun finalizado(clientes: MutableList<Cliente>) {

                    if (clientes.size == 1) {

                        BaseAsyncTask<Token?>(object :
                            BaseAsyncTask.ListenerBaseAsyncExecuta<Token?> {
                            override fun executando(): Token? {
                                val token = tokenDao.buscaToken(clientes[0].id)

                                return token

                            }

                        },
                            object : BaseAsyncTask.ListenerBaseAsyncFinaliza<Token?> {
                                override fun finalizado(token: Token?) {
                                    if (token != null) {
                                        val call =
                                            loginService?.verifyToken("Bearer ${token.token}")
                                        call?.enqueue(
                                            BaseCallback(
                                                context,
                                                object : BaseCallback.CallbackResposta<Boolean> {
                                                    override fun quandoSucesso(dados: Boolean?) {
                                                        if (dados == true) {
                                                            var login: LoginResponse =
                                                                LoginResponse(clientes[0], token)
                                                            callbackRepositoryLogin.quandoSucesso(
                                                                login
                                                            )
                                                        } else {
                                                            deletaTokenTask(
                                                                token,
                                                                callbackRepositoryLogin,
                                                                "Token inválido"
                                                            )
                                                        }
                                                    }

                                                    override fun quandoFalha(mensagem: String) {

                                                        deletaTokenTask(
                                                            token,
                                                            callbackRepositoryLogin,
                                                            mensagem
                                                        )
                                                        callbackRepositoryLogin.quandoFalha(mensagem)
                                                    }
                                                })
                                        )

                                    } else {
                                        callbackRepositoryLogin.quandoFalha("")
                                    }
                                }
                            }
                        ).execute()


                    } else if (clientes.size > 1) {
                        callbackRepositoryLogin.quandoFalha("Mais de um usuário logado!")
                    } else {
                        callbackRepositoryLogin.quandoFalha("")
                    }
                }
            }).execute()
    }

    private fun deletaTokenTask(
        token: Token,
        callbackRepositoryLogin: CallbackRepositoryLogin<LoginResponse?>,
        mensagem: String
    ) {
        BaseAsyncTask<Int>(object :
            BaseAsyncTask.ListenerBaseAsyncExecuta<Int> {
            override fun executando(): Int {
                return tokenDao.delete(token)
            }

        },
            object :
                BaseAsyncTask.ListenerBaseAsyncFinaliza<Int> {
                override fun finalizado(result: Int) {

                    callbackRepositoryLogin.quandoFalha(
                        mensagem
                    )
                }

            }).execute()
    }

    fun deletaToken(token: Token?, callbackRepositoryLogin: CallbackRepositoryLogin<Int?>) {

        BaseAsyncTask<Int?>(object : BaseAsyncTask.ListenerBaseAsyncExecuta<Int?> {
            override fun executando(): Int? {
                return token?.let { tokenDao.delete(it) }
            }

        }, object : BaseAsyncTask.ListenerBaseAsyncFinaliza<Int?> {
            override fun finalizado(result: Int?) {
                callbackRepositoryLogin.quandoSucesso(result)
            }

        }).execute()


    }

    interface CallbackRepositoryLogin<T> {
        fun quandoSucesso(dados: T)
        fun quandoFalha(erro: String)
    }

}