package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.asynctasks.EditaPecaRoupaTask
import com.example.lavanderia_cliente.asynctasks.SalvaPecaRoupaTask
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO

class FormularioSolicitacaoDeliveryActivity : AppCompatActivity() {
    private lateinit var pecaRoupaDao: PecaRoupaDao
    private lateinit var editTextTipoPeca: EditText
    private lateinit var buttonSolicitarDelivery: Button
    private lateinit var buttonEditaPeca: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_solicitacao_delivery_layout)
        setTitle(getString(R.string.titulo_bar_formulario_solic_edicao))
        inicializaDao()
        inicializacaoBotoes()
        inicializacaoEditTextTipoPeca()
        VerificaSeEdicaoOuDelivery()
    }

    private fun inicializaDao() {
        pecaRoupaDao = LavanderiaDatabase.getAppDatabase(this).getPecaRoupaDao()
    }

    private fun VerificaSeEdicaoOuDelivery() {
        var dadosRecebidos = intent
        if (condicaoParaEdicao(dadosRecebidos)) {
            buttonEditaPeca.visibility = View.VISIBLE
            val pecaParaEdicao: PecaRoupa =
                dadosRecebidos.extras?.getSerializable(EXTRA_PECA_PARA_EDICAO) as PecaRoupa
            editTextTipoPeca.setText(pecaParaEdicao.nome)
            cliqueBotaoEdita(pecaParaEdicao)
        } else {
            buttonSolicitarDelivery.visibility = View.VISIBLE
            cliqueBotaoDelivery()
        }
    }

    private fun condicaoParaEdicao(dadosRecebidos: Intent) =
        dadosRecebidos.hasExtra(EXTRA_PECA_PARA_EDICAO)


    private fun inicializacaoEditTextTipoPeca() {
        editTextTipoPeca =
            findViewById(R.id.activity_formulario_edittext_tipo_peca)
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
        val nomePeca: String = editTextTipoPeca.text.toString()
        val pecaParaDelivery =
            PecaRoupa(
                nome = nomePeca,
                status = getString(R.string.status_esperando_coleta)
            )
        SalvaPecaRoupaTask(
            pecaRoupaDao,
            pecaParaDelivery,
            object : SalvaPecaRoupaTask.ListenerSalvaPeca {
                override fun salvo(id: Long?) {
                    if (id != null) {
                        pecaParaDelivery.id = id
                        pecaParaDelivery.posicaoNaLista = id
                        EditaPecaRoupaTask(
                            pecaRoupaDao,
                            mutableListOf(pecaParaDelivery),
                            object : EditaPecaRoupaTask.ListenerEditaPecaRoupa {
                                override fun editado(nomePeca: String?) {
                                    finish()
                                }

                            }).execute()
                    }
                }

            }).execute()


    }

    private fun cliqueBotaoEdita(pecaRoupa: PecaRoupa) {
        buttonEditaPeca.setOnClickListener {
            val nomePecaEditado: String = editTextTipoPeca.text.toString()
            pecaRoupa.nome = nomePecaEditado
            val voltaParaListaRoupas = Intent(this, ListaRoupasActivity::class.java)
            voltaParaListaRoupas.putExtra(EXTRA_PECA_PARA_EDICAO, pecaRoupa)
            startActivity(voltaParaListaRoupas)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}