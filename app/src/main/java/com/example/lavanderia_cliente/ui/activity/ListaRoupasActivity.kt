package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.repository.RepositoryLogin
import com.example.lavanderia_cliente.ui.activity.callback.PecaRoupaItemTouchCallback
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.utils.AlertDialogUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ListaRoupasActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var adapter: ListaRoupasAdapter
    private lateinit var pecaRoupaDao: PecaRoupaDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    companion object {
        var cliente: Cliente? = null
        var token: Token? = null
        lateinit var mToogle: ActionBarDrawerToggle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_roupas_layout)
        setTitle(getString(R.string.titulo_bar_lista_roupas))

        if (intent.hasExtra(getString(R.string.extra_cliente_logado))) {
            cliente =
                intent.getSerializableExtra(getString(R.string.extra_cliente_logado)) as Cliente
        }

        if (intent.hasExtra(getString(R.string.extra_token_valido))) {
            token = intent.getSerializableExtra(getString(R.string.extra_token_valido)) as Token
        }

        configuraDrawerLayout()
        inicializaAdapter()
        InicializaBotaoDelivery()
    }

    private fun configuraDrawerLayout() {
        navigationView = findViewById<NavigationView>(R.id.activity_lista_roupas_nav_view)
        drawerLayout = findViewById<DrawerLayout>(R.id.activity_lista_roupas_drawer)
        mToogle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.mtogle_open, R.string.mtogle_close)
        mToogle.syncState()
        drawerLayout.addDrawerListener(mToogle)
        navigationView.setNavigationItemSelectedListener(this)
        var nomeUsuarioMenu = navigationView.getHeaderView(0)
        var emailUsuarioMenu = navigationView.getHeaderView(0)
        var nomeUsuarioText = nomeUsuarioMenu.findViewById<TextView>(R.id.nome_usuario_header)
        var emailUsuarioText = emailUsuarioMenu.findViewById<TextView>(R.id.email_usuario_header)
        nomeUsuarioText.text = cliente?.nome ?: ""
        emailUsuarioText.text = cliente?.email ?: ""
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
        atualizaAdapter()
    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout_header_lista_roupas -> {
                AlertDialogUtils(this).exibirDialogPadrao(getString(R.string.titulo_logout),
                    getString(R.string.mensagem_logout),
                    object : AlertDialogUtils.CallBackDialog {
                        override fun cliqueBotaoConfirma() {

                            RepositoryLogin(this@ListaRoupasActivity).deletaToken(token,
                                object : RepositoryLogin.CallbackRepositoryLogin<Int?> {
                                    override fun quandoSucesso(dados: Int?) {
                                        val intent = Intent(
                                            this@ListaRoupasActivity,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                    }

                                    override fun quandoFalha(erro: String) {
                                        ToastUtils().showCenterToastShort(
                                            this@ListaRoupasActivity,
                                            getString(R.string.mensagem_falha_deslogar)
                                        )
                                    }

                                })

                        }

                        override fun cliqueBotaoCancela() {

                        }


                    })
            }
        }
        return true
    }


}