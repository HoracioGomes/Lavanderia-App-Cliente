package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa


class FormularioSolicitacaoDeliveryActivity : AppCompatActivity() {
    private lateinit var dao: PecaRoupaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_solicitacao_delivery_layout)
        val buttonSolicitarDelivery: Button =
            findViewById(R.id.activity_formulario_button_solicitar_coleta)
        dao = PecaRoupaDao()
        clicaBotaoDelivery(buttonSolicitarDelivery)
    }

    private fun clicaBotaoDelivery(buttonSolicitarDelivery: Button) {
        buttonSolicitarDelivery.setOnClickListener {
            val editTextTipoPeca: EditText =
                findViewById(R.id.activity_formulario_edittext_tipo_peca)
            val nomePeca: String = editTextTipoPeca.text.toString()
            val pecaParaDelivery =
                PecaRoupa(nome = nomePeca, status = getString(R.string.status_esperando_coleta))
            val voltaParaListaRoupas = Intent()
            voltaParaListaRoupas.putExtra(getString(R.string.extra_peca_roupa), pecaParaDelivery)
            setResult(ConstantesInt.CODIGO_RESULTADO_REQUISICAO_PECA_DELIVERY.valor, voltaParaListaRoupas)
            finish()

        }
    }

}