package com.example.lavanderia_cliente.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.ui.viewmodel.UsuarioViewModel
import com.example.lavanderia_cliente.ui.viewmodel.factory.UsuarioViewModelFactory
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.ProgressBarUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val clienteDao: ClienteDao by lazy {
        LavanderiaDatabase.getAppDatabase(this).getClienteDao()
    }

    private val tokenDao: TokenDao by lazy {
        LavanderiaDatabase.getAppDatabase(this).getTokenDao()
    }

    private val viewModelUsuario by lazy {
        val repository = RepositoryUsuario(clienteDao, tokenDao)
        val provider = ViewModelProviders.of(this, UsuarioViewModelFactory(repository))
        provider.get(UsuarioViewModel::class.java)
    }

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

        viewModelUsuario.loginAutomatico()
            .observe(this, Observer { resposta ->
                var spinner: Dialog = ProgressBarUtils.mostraProgressBar(context)
                if (resposta.dados != null) {
                    spinner.dismiss()
                    val intent = Intent(context, ListaRoupasActivity::class.java)
                    intent.putExtra(
                        getString(R.string.extra_cliente_logado),
                        resposta?.dados?.cliente
                    )
                    intent.putExtra(
                        getString(R.string.extra_token_valido),
                        resposta?.dados?.token
                    )
                    startActivity(intent)
                } else {
                    if (resposta.erro != null) {
                        spinner.dismiss()
                    }
                }
            })
    }

    private fun loginManual(context: Context, email: String, senha: String) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            var spinner: Dialog = ProgressBarUtils.mostraProgressBar(context)
            viewModelUsuario.logar(email = email, senha = senha)
                .observe(this, Observer { resposta ->
                    spinner.dismiss()
                    if (resposta.erro == null && resposta.dados != null) {

                        ToastUtils().showCenterToastShort(
                            context,
                            "${resposta?.dados.cliente.nome} foi logado!"
                        )
                        val intent = Intent(context, ListaRoupasActivity::class.java)
                        intent.putExtra(
                            getString(R.string.extra_cliente_logado),
                            resposta.dados.cliente
                        )
                        intent.putExtra(
                            getString(R.string.extra_token_valido),
                            resposta.dados.token
                        )
                        startActivity(intent)
                    } else if (resposta.erro != null) {
                        spinner.dismiss()

                        ToastUtils().showCenterToastShort(
                            context,
                            "Erro ao logar!\n${resposta.erro}"
                        )

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