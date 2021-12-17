package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.ui.viewmodel.factory.PecaRoupaViewModelFactory
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import com.example.lavanderia_cliente.utils.DataUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import java.util.*

class FormularioSolicitacaoDeliveryActivity : AppCompatActivity() {
    private lateinit var editTextNomePeca: EditText
    private lateinit var buttonSolicitarDelivery: Button
    private lateinit var buttonEditaPeca: Button

    private val pecaRoupaDao: PecaRoupaDao by lazy {
        LavanderiaDatabase.getAppDatabase(this).getPecaRoupaDao()
    }

    private val viewModelPecaRoupa by lazy {
        val repositoryPecaRoupa: RepositoryPecaRoupa = RepositoryPecaRoupa(pecaRoupaDao)
        val provider = ViewModelProviders.of(this, PecaRoupaViewModelFactory(repositoryPecaRoupa))
        provider.get(PecaRoupaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_solicitacao_delivery_layout)
        setTitle(getString(R.string.titulo_bar_formulario_solic_edicao))
        inicializacaoBotoes()
        inicializacaoEditTextNomePeca()
        VerificaSeEdicaoOuDelivery()
    }


    private fun VerificaSeEdicaoOuDelivery() {
        var dadosRecebidos = intent
        if (condicaoParaEdicao(dadosRecebidos)) {
            buttonEditaPeca.visibility = View.VISIBLE
            val pecaParaEdicao: PecaRoupa =
                dadosRecebidos.extras?.getSerializable(EXTRA_PECA_PARA_EDICAO) as PecaRoupa
            editTextNomePeca.setText(pecaParaEdicao.nome)
            cliqueBotaoEdita(pecaParaEdicao)
        } else {
            buttonSolicitarDelivery.visibility = View.VISIBLE
            cliqueBotaoDelivery()
        }
    }

    private fun condicaoParaEdicao(dadosRecebidos: Intent) =
        dadosRecebidos.hasExtra(EXTRA_PECA_PARA_EDICAO)


    private fun inicializacaoEditTextNomePeca() {
        editTextNomePeca =
            findViewById(R.id.activity_formulario_edittext_nome_peca)
    }


    private fun inicializacaoBotoes() {
        buttonSolicitarDelivery =
            findViewById(R.id.activity_formulario_button_solicitar_coleta)
        buttonEditaPeca = findViewById(R.id.activity_formulario_button_edita_peca)
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
        if (ConnectionManagerUtils().checkInternetConnection(this) == 1) {
            viewModelPecaRoupa.salva(pecaParaDelivery).observe(
                this, {
                    if (it.erro == null) {
                        ToastUtils().showCenterToastShort(this, "Salvo!")
                        finish()
                    } else {
                        it.erro?.let { erro -> ToastUtils().showCenterToastShort(this, erro) }
                    }

                }
            )
        } else {
            ToastUtils().showCenterToastShort(
                this,
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
        if (ConnectionManagerUtils().checkInternetConnection(this) == 1) {
            val nomePecaEditado: String = editTextNomePeca.text.toString()
            pecaRoupa.nome = nomePecaEditado

            viewModelPecaRoupa.edita(pecaRoupa).observe(this,
                {
                    if (it.erro == null) {
                        ToastUtils().showCenterToastShort(
                            this@FormularioSolicitacaoDeliveryActivity,
                            "Nome Alterado: ${it.dados?.nome}"
                        )
                        finish()
                    } else {
                        ToastUtils().showCenterToastShort(
                            this@FormularioSolicitacaoDeliveryActivity,
                            "${it.erro}"
                        )
                    }
                }
            )

        } else {
            ToastUtils().showCenterToastShort(this, getString(R.string.mensagen_conectese_a_rede))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}