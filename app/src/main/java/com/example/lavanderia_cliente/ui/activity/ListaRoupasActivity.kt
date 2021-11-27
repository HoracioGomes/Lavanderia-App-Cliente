package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.asynctasks.EditaPecaRoupaTask
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.activity.callback.PecaRoupaItemTouchCallback
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaRoupasActivity : AppCompatActivity() {
    private lateinit var adapter: ListaRoupasAdapter
    private lateinit var pecaRoupaDao: PecaRoupaDao
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_roupas_layout)
        setTitle(getString(R.string.titulo_bar_lista_roupas))
        inicializaAdapter()
        InicializaBotaoDelivery()
    }

    private fun inicializaAdapter() {
        recyclerView = findViewById(R.id.activity_lista_roupas_recyclerview)
        inicializaDao()
        adapter = ListaRoupasAdapter(this, pecaRoupaDao)
        recyclerView.adapter = adapter
        configuraSwipe()
    }

    private fun atualizaAdapter() {
        adapter.atualiza()
    }

    private fun inicializaDao() {
        pecaRoupaDao = LavanderiaDatabase.getAppDatabase(context = this).getPecaRoupaDao()
    }

    private fun configuraSwipe() {
        var itemTouchHelper = ItemTouchHelper(PecaRoupaItemTouchCallback(this, adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun InicializaBotaoDelivery() {
        var btnDelivery =
            findViewById<FloatingActionButton>(R.id.activity_lista_roupas_fab_delivery)
        btnDelivery.setOnClickListener {
            val vaiParaFormularioDelivery =
                Intent(this, FormularioSolicitacaoDeliveryActivity::class.java)
            startActivity(
                vaiParaFormularioDelivery
            )

        }
    }

    override fun onResume() {
        super.onResume()
        populaAdapter()
    }

    private fun populaAdapter() {
        var dadosRecebidos = intent
        if (verificaSeVoltouDaEdicao(dadosRecebidos)) {
            val pecaRoupa =
                dadosRecebidos.getSerializableExtra(EXTRA_PECA_PARA_EDICAO) as PecaRoupa
            EditaPecaRoupaTask(
                pecaRoupaDao,
                mutableListOf(pecaRoupa),
                object : EditaPecaRoupaTask.ListenerEditaPecaRoupa {
                    override fun editado(nomePeca: String?) {
                        atualizaAdapter()
                    }
                }).execute()
        } else {
            atualizaAdapter()
        }
    }

    private fun verificaSeVoltouDaEdicao(dadosRecebidos: Intent) =
        dadosRecebidos.hasExtra(EXTRA_PECA_PARA_EDICAO)

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}