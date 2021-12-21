package com.example.lavanderia_cliente.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.ui.viewmodel.factory.PecaRoupaViewModelFactory
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.Constantes
import com.example.lavanderia_cliente.utils.DataUtils
import com.example.lavanderia_cliente.utils.ToastUtils

class FormularioDeliveryFragment : Fragment() {
    private lateinit var editTextNomePeca: EditText
    private lateinit var buttonSolicitarDelivery: Button
    private lateinit var buttonEditaPeca: Button
    private val pecaRoupaDao: PecaRoupaDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getPecaRoupaDao()
    }

    private val viewModelPecaRoupa by lazy {
        val repositoryPecaRoupa = RepositoryPecaRoupa(pecaRoupaDao)
        val provider = ViewModelProviders.of(this, PecaRoupaViewModelFactory(repositoryPecaRoupa))
        provider.get(PecaRoupaViewModel::class.java)
    }

    var quandoFinish: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.formulario_delivery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.titulo_bar_formulario_solic_edicao)
        inicializacaoBotoes(view)
        inicializacaoEditTextNomePeca(view)
        VerificaSeEdicaoOuDelivery()
    }


    private fun VerificaSeEdicaoOuDelivery() {
        var dadosRecebidos = arguments
        if (condicaoParaEdicao(dadosRecebidos)) {
            buttonEditaPeca.visibility = View.VISIBLE
            val pecaParaEdicao: PecaRoupa =
                dadosRecebidos?.getSerializable(Constantes.EXTRA_PECA_PARA_EDICAO) as PecaRoupa
            editTextNomePeca.setText(pecaParaEdicao.nome)
            cliqueBotaoEdita(pecaParaEdicao)
        } else {
            buttonSolicitarDelivery.visibility = View.VISIBLE
            cliqueBotaoDelivery()
        }
    }

    private fun condicaoParaEdicao(dadosRecebidos: Bundle?): Boolean {
        return dadosRecebidos?.getSerializable(Constantes.EXTRA_PECA_PARA_EDICAO) != null
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
                        quandoFinish()
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

    private fun cliqueBotaoEdita(pecaRoupa: PecaRoupa) {
        buttonEditaPeca.setOnClickListener {
            editaPecaRoupa(pecaRoupa)
        }
    }

    private fun editaPecaRoupa(pecaRoupa: PecaRoupa) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            val nomePecaEditado: String = editTextNomePeca.text.toString()
            pecaRoupa.nome = nomePecaEditado

            viewModelPecaRoupa.edita(pecaRoupa).observe(context as LifecycleOwner,
                {
                    if (it.erro == null) {
                        arguments?.clear()
                        editTextNomePeca.text.clear()
                        ToastUtils().showCenterToastShort(
                            context,
                            "Nome Alterado: ${it.dados?.nome}"
                        )
                        quandoFinish()
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


}