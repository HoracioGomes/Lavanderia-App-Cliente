package com.example.lavanderia_cliente.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.repository.RepositoryLogin
import com.example.lavanderia_cliente.retrofit.responses.LoginResponse
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.ProgressBarUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_login_layout)
        val edtTxtEmail = findViewById<TextInputEditText>(R.id.edttext_email_login)
        val edtTxtSenha = findViewById<TextInputEditText>(R.id.edttext_senha_login)
        val buttonLogar = findViewById<Button>(R.id.button_logar)
        val email = edtTxtEmail.text
        val senha = edtTxtSenha.text


        tentaLoginAutomatico(this)

        buttonLogar.setOnClickListener {
            loginManual(this, email = email.toString(), senha = senha.toString())
        }

    }

    fun tentaLoginAutomatico(context: Context) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            var spinner: Dialog = ProgressBarUtils.mostraProgressBar(context)

            RepositoryLogin(context).loginAutomatico(object :
                RepositoryLogin.CallbackRepositoryLogin<LoginResponse?> {
                override fun quandoSucesso(dados: LoginResponse?) {
                    spinner.dismiss()
                    val intent = Intent(context, ListaRoupasActivity::class.java)
                    intent.putExtra(getString(R.string.extra_cliente_logado), dados?.cliente)
                    intent.putExtra(getString(R.string.extra_token_valido), dados?.token)
                    startActivity(intent)
                }

                override fun quandoFalha(erro: String) {
                    spinner.dismiss()
                    if (erro != "") {
                        ToastUtils().showCenterToastShort(
                            this@LoginActivity,
                            erro
                        )
                    }
                }

            })
        } else {
            ToastUtils().showCenterToastShort(this, getString(R.string.mensagen_conectese_a_rede))

        }
    }

    private fun loginManual(context: Context, email: String, senha: String) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            var spinner: Dialog = ProgressBarUtils.mostraProgressBar(context)
            RepositoryLogin(this).auth(
                email,
                senha,
                object : RepositoryLogin.CallbackRepositoryLogin<LoginResponse?> {
                    override fun quandoSucesso(dados: LoginResponse?) {
                        spinner.dismiss()
                        ToastUtils().showCenterToastShort(
                            context,
                            "${dados?.cliente?.nome} foi logado!"
                        )
                        val intent = Intent(context, ListaRoupasActivity::class.java)
                        intent.putExtra(getString(R.string.extra_cliente_logado), dados?.cliente)
                        intent.putExtra(getString(R.string.extra_token_valido), dados?.token)
                        startActivity(intent)
                    }

                    override fun quandoFalha(erro: String) {
                        spinner.dismiss()
                        ToastUtils().showCenterToastShort(context, "Erro ao logar!\n$erro")

                    }

                })
        } else {
            ToastUtils().showCenterToastShort(this, getString(R.string.mensagen_conectese_a_rede))

        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}