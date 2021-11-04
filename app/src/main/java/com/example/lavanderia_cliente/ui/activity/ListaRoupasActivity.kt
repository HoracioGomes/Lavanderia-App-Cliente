package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.CODIGO_REQUISICAO_PECA_DELIVERY
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.CODIGO_RESULTADO_REQUISICAO_PECA_DELIVERY
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_ROUPA
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_POSICAO_PECA_PARA_EDICAO
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaRoupasActivity : AppCompatActivity() {
    private lateinit var adapter: ListaRoupasAdapter
    private val dao: PecaRoupaDao = PecaRoupaDao()
    private val listaRoupas: MutableList<PecaRoupa> = mutableListOf()
    private val POSICAO_INVALIDA = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_roupas_layout)
        inicializaAdapter()
        InicializaBotaoDelivery()
        verificaSeHouveEdicao()
    }

    private fun inicializaAdapter() {
        val layoutLista: RecyclerView = findViewById(R.id.activity_lista_roupas_recyclerview)
        listaRoupas.addAll(dao.todas())
        adapter = ListaRoupasAdapter(this, listaRoupas)
        layoutLista.adapter = adapter
    }


    private fun verificaSeHouveEdicao() {
        var dadosRecebidos = intent
        if (condicaoParaEdicao(dadosRecebidos)) {
            var pecaRoupa =
                dadosRecebidos.getSerializableExtra(EXTRA_PECA_PARA_EDICAO) as PecaRoupa
            var posicao =
                dadosRecebidos.getIntExtra(
                    EXTRA_POSICAO_PECA_PARA_EDICAO,
                    POSICAO_INVALIDA
                )
            dao.altera(posicao, pecaRoupa)
            adapter.alteraPecaRoupa(posicao, pecaRoupa)
        }
    }

    private fun condicaoParaEdicao(dadosRecebidos: Intent) =
        dadosRecebidos.hasExtra(EXTRA_PECA_PARA_EDICAO) && dadosRecebidos.hasExtra(
            EXTRA_POSICAO_PECA_PARA_EDICAO
        )


    private fun InicializaBotaoDelivery() {
        var btnDelivery =
            findViewById<FloatingActionButton>(R.id.activity_lista_roupas_fab_delivery)
        btnDelivery.setOnClickListener {
            val vaiParaFormularioDelivery =
                Intent(this, FormularioSolicitacaoDeliveryActivity::class.java)
            startActivityForResult(
                vaiParaFormularioDelivery,
                CODIGO_REQUISICAO_PECA_DELIVERY
            )

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == CODIGO_REQUISICAO_PECA_DELIVERY &&
                resultCode == CODIGO_RESULTADO_REQUISICAO_PECA_DELIVERY &&
                data.hasExtra(
                    EXTRA_PECA_ROUPA
                )
            ) {
                val pecaRoupa: PecaRoupa =
                    data.getSerializableExtra(EXTRA_PECA_ROUPA) as PecaRoupa
                dao.insere(pecaRoupa)
                adapter.adicionaPecaRoupa(pecaRoupa)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}