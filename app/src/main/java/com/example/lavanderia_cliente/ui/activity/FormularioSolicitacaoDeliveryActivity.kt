package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.CODIGO_RESULTADO_REQUISICAO_PECA_DELIVERY
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_ROUPA
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_POSICAO_PECA_PARA_EDICAO


class FormularioSolicitacaoDeliveryActivity : AppCompatActivity() {
    private lateinit var dao: PecaRoupaDao
    private lateinit var editTextTipoPeca: EditText
    private lateinit var buttonSolicitarDelivery: Button
    private lateinit var buttonEditaPeca: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_solicitacao_delivery_layout)
        inicializacaoBotoes()
        inicializacaoDao()
        inicializacaoEditTextTipoPeca()
        AlternaSeEdicaoOuSolicitacao()
    }

    private fun AlternaSeEdicaoOuSolicitacao() {
        var dadosRecebidos = intent
        if (condicaoParaEdicao(dadosRecebidos)) {
            buttonEditaPeca.visibility = View.VISIBLE
            val pecaParaEdicao: PecaRoupa =
                dadosRecebidos.extras?.getSerializable(EXTRA_PECA_PARA_EDICAO) as PecaRoupa
            val posicao: Int =
                dadosRecebidos.extras?.getSerializable(EXTRA_POSICAO_PECA_PARA_EDICAO) as Int
            editTextTipoPeca.setText(pecaParaEdicao.nome)
            cliqueBotaoEdita(pecaParaEdicao, posicao)
        } else {
            buttonSolicitarDelivery.visibility = View.VISIBLE
            cliqueBotaoDelivery()

        }
    }

    private fun condicaoParaEdicao(dadosRecebidos: Intent) =
        dadosRecebidos.hasExtra(EXTRA_PECA_PARA_EDICAO) && dadosRecebidos.hasExtra(
            EXTRA_POSICAO_PECA_PARA_EDICAO
        )

    private fun inicializacaoEditTextTipoPeca() {
        editTextTipoPeca =
            findViewById(R.id.activity_formulario_edittext_tipo_peca)
    }

    private fun inicializacaoDao() {
        dao = PecaRoupaDao()
    }

    private fun inicializacaoBotoes() {
        buttonSolicitarDelivery =
            findViewById(R.id.activity_formulario_button_solicitar_coleta)
        buttonEditaPeca = findViewById(R.id.activity_formulario_button_edita_peca)
    }

    private fun cliqueBotaoDelivery() {
        buttonSolicitarDelivery.setOnClickListener {
            val nomePeca: String = editTextTipoPeca.text.toString()
            val pecaParaDelivery =
                PecaRoupa(nome = nomePeca, status = getString(R.string.status_esperando_coleta))
            val voltaParaListaRoupas = Intent()
            voltaParaListaRoupas.putExtra(EXTRA_PECA_ROUPA, pecaParaDelivery)
            setResult(
                CODIGO_RESULTADO_REQUISICAO_PECA_DELIVERY,
                voltaParaListaRoupas
            )
            finish()

        }
    }

    private fun cliqueBotaoEdita(pecaRoupa: PecaRoupa, posicao: Int) {
        buttonEditaPeca.setOnClickListener {
            val nomePecaEditado: String = editTextTipoPeca.text.toString()
            pecaRoupa.nome = nomePecaEditado
            val voltaParaListaRoupas = Intent(this, ListaRoupasActivity::class.java)
            voltaParaListaRoupas.putExtra(EXTRA_PECA_PARA_EDICAO, pecaRoupa)
            voltaParaListaRoupas.putExtra(
                EXTRA_POSICAO_PECA_PARA_EDICAO,
                posicao
            )
            startActivity(voltaParaListaRoupas)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}