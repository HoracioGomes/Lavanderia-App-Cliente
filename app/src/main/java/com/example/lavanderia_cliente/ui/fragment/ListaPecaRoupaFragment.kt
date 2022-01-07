package com.example.lavanderia_cliente.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.databinding.ListaRoupasFragmentBinding
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.cliente
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.mToogle
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.token
import com.example.lavanderia_cliente.ui.activity.MainActivity.Companion.viewModelEstado
import com.example.lavanderia_cliente.ui.activity.callback.PecaRoupaItemTouchCallback
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.ui.viewmodel.ComponetesVisuais
import com.example.lavanderia_cliente.utils.AlertDialogUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.navigation.NavigationView

class ListaPecaRoupaFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {

    private var adapter: ListaRoupasAdapter? = null
//    private val adapter: ListaRoupasAdapter? by inject{
//        parametersOf(findNavController())
//    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = ListaRoupasFragmentBinding.inflate(inflater, container, false)
        viewBinding.clickListener = View.OnClickListener {
            vaiParaFormulario()
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelEstado.temComponentes = ComponetesVisuais(appBar = true, bottomNavigation = true)
        configuraDrawerLayout(view)
        context?.let { inicializaAdapter(view, it) }
    }


    private fun vaiParaFormulario() {
        val acttion =
            ListaPecaRoupaFragmentDirections.actionListaPecasRoupasToFormularioDelivery()
        navControler.navigate(acttion)
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
        adapter = ListaRoupasAdapter(
            context, viewModelPecasRoupa,
            navControler
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
                                    viewModelUsuario.deletaTokenLiveData
                                        .observe(context as LifecycleOwner,
                                            Observer { resourcePosDelecao ->

                                                if (resourcePosDelecao?.dados != null) {
                                                    vaiParaLogin()
                                                } else {
                                                    ToastUtils().showCenterToastShort(
                                                        context!!,
                                                        getString(R.string.mensagem_falha_deslogar)
                                                    )
                                                }
                                            })

                                    viewModelUsuario.deletarToken(token)

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

    private fun vaiParaLogin() {
        val action =
            ListaPecaRoupaFragmentDirections.actionGlobalLogout()
        navControler.popBackStack()
        navControler.navigate(action)
    }


}