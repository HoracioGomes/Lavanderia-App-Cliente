package com.example.lavanderia_cliente.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.cliente
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.token
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.viewModelEstado
import com.example.lavanderia_cliente.ui.viewmodel.ComponetesVisuais
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.ProgressBarUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tela_login_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelEstado.temComponentes = ComponetesVisuais()
        val edtTxtEmail = view.findViewById<TextInputEditText>(R.id.edttext_email_login)
        val edtTxtSenha = view.findViewById<TextInputEditText>(R.id.edttext_senha_login)
        val buttonLogar = view.findViewById<Button>(R.id.button_logar)
        val email = edtTxtEmail.text
        val senha = edtTxtSenha.text


        context?.let { context -> tentaLoginAutomatico(context) }

        buttonLogar.setOnClickListener {
            context?.let { context ->
                loginManual(
                    context,
                    email = email.toString(),
                    senha = senha.toString()
                )
            }
        }

    }


    private fun loginManual(context: Context, email: String, senha: String) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            val spinner: Dialog = ProgressBarUtils.mostraProgressBar(context)
            viewModelUsuario.loginManualLiveData
                .observe(context as LifecycleOwner, Observer { resposta ->

                    if (resposta?.erro == null && resposta?.dados != null) {

                        cliente = resposta.dados.cliente
                        token = resposta.dados.token

                        ToastUtils().showCenterToastShort(
                            context,
                            "${resposta.dados.cliente?.nome} foi logado!"
                        )
                        spinner.dismiss()
                        vaiParaListaPecasRoupas()

                    } else if (resposta?.erro != null) {
                        spinner.dismiss()

                        ToastUtils().showCenterToastShort(
                            context,
                            "Erro ao logar!\n${resposta.erro}"
                        )

                    }

                })

            viewModelUsuario.logar(email = email, senha = senha)

        } else {
            ToastUtils().showCenterToastShort(
                context,
                getString(R.string.mensagen_conectese_a_rede)
            )

        }
    }

    fun tentaLoginAutomatico(context: Context) {
        viewModelUsuario.loginAutomaticoLiveData
            .observe(context as LifecycleOwner, Observer { resposta ->
                val spinner: Dialog = ProgressBarUtils.mostraProgressBar(context)
                if (resposta?.dados?.token != null && resposta.dados.cliente != null) {
                    cliente =
                        resposta.dados.cliente

                    token = resposta.dados.token
                    spinner.dismiss()
                    vaiParaListaPecasRoupas()

                }
                spinner.dismiss()
            })

        viewModelUsuario.loginAutomatico()
    }

    private fun vaiParaListaPecasRoupas() {
        val action = LoginFragmentDirections.actionLoginToListaPecasRoupas()
        navControler.navigate(action)
    }

}