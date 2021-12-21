package com.example.lavanderia_cliente.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
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
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.ui.activity.LoginActivity
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

class ListaPecaRoupaFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private var adapter: ListaRoupasAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private val pecaRoupaDao: PecaRoupaDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getPecaRoupaDao()
    }

    private val clienteDao: ClienteDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getClienteDao()
    }

    private val tokenDao: TokenDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getTokenDao()
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

    var quandoBtnFabClicado: () -> Unit = {}

    companion object {
        var cliente: Cliente? = null
        var token: Token? = null
        lateinit var mToogle: ActionBarDrawerToggle
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lista_roupas_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.titulo_bar_lista_roupas)
        configuraDrawerLayout(view)
        context?.let { inicializaAdapter(view, it) }
        inicializaBtnFabDelivery(view)
    }

    private fun inicializaBtnFabDelivery(view: View) {
        val btnFabSolicitacaoDelivery =
            view.findViewById<FloatingActionButton>(R.id.lista_roupas_fab_delivery)
        btnFabSolicitacaoDelivery.setOnClickListener {
            quandoBtnFabClicado()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun configuraDrawerLayout(view: View) {
        navigationView = view.findViewById<NavigationView>(R.id.lista_roupas_nav_view)
        drawerLayout = view.findViewById<DrawerLayout>(R.id.activity_lista_roupas_drawer)
        mToogle =
            ActionBarDrawerToggle(
                activity,
                drawerLayout,
                R.string.mtogle_open,
                R.string.mtogle_close
            )
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

    private fun inicializaAdapter(view: View, context: Context) {
        recyclerView = view.findViewById(R.id.lista_roupas_recyclerview)
        adapter = ListaRoupasAdapter(context, viewModelListaRoupas,
            activity?.supportFragmentManager
        )
        recyclerView.adapter = adapter
        adapter?.atualiza()
        configuraSwipe()
    }

    private fun configuraSwipe() {
        var itemTouchHelper = context?.let { PecaRoupaItemTouchCallback(it, adapter) }?.let {
            ItemTouchHelper(
                it
            )
        }
        itemTouchHelper?.attachToRecyclerView(recyclerView)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout_header_lista_roupas -> {
                context?.let {
                    AlertDialogUtils(it).exibirDialogPadrao(getString(R.string.titulo_logout),
                        getString(R.string.mensagem_logout),
                        object : AlertDialogUtils.CallBackDialog {
                            override fun cliqueBotaoConfirma() {


                                token?.let { token ->
                                    viewModelUsuario.deletarToken(token)
                                        .observe(context as LifecycleOwner,
                                            Observer { resourcePosDelecao ->

                                                if (resourcePosDelecao.dados != null) {
                                                    val intent = Intent(
                                                        context,
                                                        LoginActivity::class.java
                                                    )
                                                    startActivity(intent)
                                                } else {
                                                    ToastUtils().showCenterToastShort(
                                                        context!!,
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
        }
        return true
    }

}