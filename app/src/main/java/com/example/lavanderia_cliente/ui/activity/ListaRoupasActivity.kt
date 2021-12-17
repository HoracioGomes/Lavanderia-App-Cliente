package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.ui.activity.callback.PecaRoupaItemTouchCallback
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.ui.viewmodel.UsuarioViewModel
import com.example.lavanderia_cliente.ui.viewmodel.factory.PecaRoupaViewModelFactory
import com.example.lavanderia_cliente.ui.viewmodel.factory.UsuarioViewModelFactory
import com.example.lavanderia_cliente.utils.AlertDialogUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ListaRoupasActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var adapter: ListaRoupasAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private val pecaRoupaDao: PecaRoupaDao by lazy {
        LavanderiaDatabase.getAppDatabase(this).getPecaRoupaDao()
    }

    private val clienteDao: ClienteDao by lazy {
        LavanderiaDatabase.getAppDatabase(this).getClienteDao()
    }

    private val tokenDao: TokenDao by lazy {
        LavanderiaDatabase.getAppDatabase(this).getTokenDao()
    }

    private val viewModelListaRoupas by lazy {
        val repository = RepositoryPecaRoupa(pecaRoupaDao)
        val provider = ViewModelProviders.of(this, PecaRoupaViewModelFactory(repository))
        provider.get(PecaRoupaViewModel::class.java)
    }

    private val viewModelUsuario by lazy {
        val repository = RepositoryUsuario(clienteDao, tokenDao)
        val provider = ViewModelProviders.of(this, UsuarioViewModelFactory(repository))
        provider.get(UsuarioViewModel::class.java)
    }


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
        adapter = ListaRoupasAdapter(this, viewModelListaRoupas)
        recyclerView.adapter = adapter
        adapter.atualiza()
        configuraSwipe()
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


                            token?.let { token ->
                                viewModelUsuario.
                                        deletarToken(token).observe(this@ListaRoupasActivity,
                                    Observer { resourcePosDelecao ->

                                        if (resourcePosDelecao.dados != null){
                                            val intent = Intent(
                                                this@ListaRoupasActivity,
                                                LoginActivity::class.java
                                            )
                                            startActivity(intent)
                                        }else{
                                            ToastUtils().showCenterToastShort(
                                                this@ListaRoupasActivity,
                                                getString(R.string.mensagem_falha_deslogar)
                                            )
                                        }
                                    })

                            }


                        }

                        override fun cliqueBotaoCancela() {

                        }


                    })
            }
        }
        return true
    }


}