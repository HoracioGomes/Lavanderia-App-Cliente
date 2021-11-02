package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaRoupasActivity : AppCompatActivity() {
    private lateinit var adapter: ListaRoupasAdapter
    private val dao: PecaRoupaDao = PecaRoupaDao()
    private val listaRoupas: MutableList<PecaRoupa> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_roupas_layout)
        val layoutLista: RecyclerView = findViewById(R.id.activity_lista_roupas_recyclerview)
        listaRoupas.addAll(dao.todas())
        adapter = ListaRoupasAdapter(this, listaRoupas)
        layoutLista.adapter = adapter
        cliqueBotaoDelivery()
    }


    fun cliqueBotaoDelivery() {
        vaiParaFormularioSolicitacaoDelivery()
    }

    private fun ListaRoupasActivity.vaiParaFormularioSolicitacaoDelivery() {
        var btnDelivery =
            findViewById<FloatingActionButton>(R.id.activity_lista_roupas_fab_delivery)
        btnDelivery.setOnClickListener {
            val vaiParaFormularioDelivery =
                Intent(this, FormularioSolicitacaoDeliveryActivity::class.java)
            startActivityForResult(
                vaiParaFormularioDelivery,
                ConstantesInt.CODIGO_REQUISICAO_PECA_DELIVERY.valor
            )

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == ConstantesInt.CODIGO_REQUISICAO_PECA_DELIVERY.valor &&
                resultCode == ConstantesInt.CODIGO_RESULTADO_REQUISICAO_PECA_DELIVERY.valor &&
                data.hasExtra(
                    getString(R.string.extra_peca_roupa)
                )
            ) {
                val pecaRoupa: PecaRoupa =
                    data.getSerializableExtra(getString(R.string.extra_peca_roupa)) as PecaRoupa
                dao.insere(pecaRoupa)
                adapter.adicionaPecaRoupa(pecaRoupa)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }


}