package com.example.lavanderia_cliente.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.navArgs
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.DataUtils
import com.example.lavanderia_cliente.utils.ToastUtils

class FormularioDeliveryFragment : BaseFragment() {
    private lateinit var editTextNomePeca: EditText
    private lateinit var buttonSolicitarDelivery: Button
    private lateinit var buttonEditaPeca: Button

    private val argumentos = navArgs<FormularioDeliveryFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.formulario_delivery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializacaoBotoes(view)
        inicializacaoEditTextNomePeca(view)
        VerificaSeEdicaoOuDelivery()
    }


    private fun VerificaSeEdicaoOuDelivery() {

        if (condicaoParaEdicao()) {
            val pecaRoupaEdicao = argumentos.value.pecaEdicao
            buttonEditaPeca.visibility = View.VISIBLE
            editTextNomePeca.setText(pecaRoupaEdicao?.nome)
            cliqueBotaoEdita(pecaRoupaEdicao)
        } else {
            buttonSolicitarDelivery.visibility = View.VISIBLE
            cliqueBotaoDelivery()
        }
    }

    private fun condicaoParaEdicao(): Boolean {

        return argumentos.value.pecaEdicao != null
    }


    private fun inicializacaoEditTextNomePeca(view: View) {
        editTextNomePeca =
            view?.findViewById(R.id.activity_formulario_edittext_nome_peca)
    }


    private fun inicializacaoBotoes(view: View) {
        buttonSolicitarDelivery =
            view.findViewById(R.id.activity_formulario_button_solicitar_coleta)
        buttonEditaPeca = view.findViewById(R.id.activity_formulario_button_edita_peca)
    }

    private fun cliqueBotaoDelivery() {
        buttonSolicitarDelivery.setOnClickListener {
            salvaPecaRoupa()
        }
    }

    private fun salvaPecaRoupa() {
        val nomePeca: String = editTextNomePeca.text.toString()
        val pecaParaDelivery =
            PecaRoupa(
                nome = nomePeca,
                status = getString(R.string.status_esperando_coleta),
                data = DataUtils().dataAtualParaBanco()
            )
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            viewModelPecaRoupa.salva(pecaParaDelivery).observe(
                context as LifecycleOwner, {
                    if (it.erro == null) {
                        editTextNomePeca.text.clear()
                        ToastUtils().showCenterToastShort(
                            context,
                            "Salvo!"
                        )
                        vaiParaListaRoupas()
                    } else {
                        ToastUtils().showCenterToastShort(
                            context,
                            "${it.erro}"
                        )
                    }

                }
            )
        } else {
            ToastUtils().showCenterToastShort(
                context,
                this.getString(R.string.mensagen_conectese_a_rede)
            )
        }

    }


    private fun cliqueBotaoEdita(pecaRoupa: PecaRoupa?) {
        buttonEditaPeca.setOnClickListener {
            editaPecaRoupa(pecaRoupa)
        }
    }

    private fun editaPecaRoupa(pecaRoupa: PecaRoupa?) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            val nomePecaEditado: String = editTextNomePeca.text.toString()
            pecaRoupa?.nome = nomePecaEditado

            viewModelPecaRoupa.edita(pecaRoupa).observe(context as LifecycleOwner,
                {
                    if (it.erro == null) {
                        arguments?.clear()
                        editTextNomePeca.text.clear()
                        ToastUtils().showCenterToastShort(
                            context,
                            "Nome Alterado: ${it.dados?.nome}"
                        )
                        vaiParaListaRoupas()
                    } else {
                        ToastUtils().showCenterToastShort(
                            context,
                            "${it.erro}"
                        )
                    }
                }
            )

        } else {
            ToastUtils().showCenterToastShort(
                context,
                getString(R.string.mensagen_conectese_a_rede)
            )
        }
    }

    private fun vaiParaListaRoupas() {
        val action =
            FormularioDeliveryFragmentDirections.actionFormularioDeliveryToListaPecasRoupas()
        navControler.navigate(action)
    }

}