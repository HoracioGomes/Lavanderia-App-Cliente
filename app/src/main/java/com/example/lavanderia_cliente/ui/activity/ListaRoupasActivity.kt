package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.adapter.ListaRoupasAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaRoupasActivity : AppCompatActivity() {
    private val logListaRoupasActivity = "LogListaRoupas"
    private lateinit var adapter: ListaRoupasAdapter
    private val dao: PecaRoupaDao  = PecaRoupaDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_roupas_layout)
        val layoutLista: ListView = findViewById(R.id.activity_lista_roupas_recyclerview)
        adapter = ListaRoupasAdapter(this, dao.todas() )
        layoutLista.adapter = adapter
        cliqueBotaoDelivery()
    }



    fun cliqueBotaoDelivery() {
        var btnDelivery =
            findViewById<FloatingActionButton>(R.id.activity_lista_roupas_fab_delivery)
        btnDelivery.setOnClickListener {
            val vaiParaFormularioDelivery =
                Intent(this, FormularioSolicitacaoDeliveryActivity::class.java)
            startActivity(vaiParaFormularioDelivery)

        }

    }

    override fun onResume() {
        super.onResume()
        atualizaAdapter()
    }

    fun atualizaAdapter() {
        adapter.notifyDataSetChanged()
    }




}