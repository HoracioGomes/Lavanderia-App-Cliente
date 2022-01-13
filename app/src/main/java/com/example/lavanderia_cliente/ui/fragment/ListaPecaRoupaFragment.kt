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
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.ProgressBarUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import com.google.android.material.navigation.NavigationView

class ListaPecaRoupaFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {

    private var erroFragment: String? = null

    private var adapter: ListaRoupasAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onResume() {
        super.onResume()
        viewModelPecasRoupa.buscaTodos().observe(this, Observer {
            it?.dados?.let { it1 ->
                adapter?.popula(it1)
                it?.erro?.let { erro ->
                    erroFragment = erro
                    //Implementar extensao Fragment mostraErro
                }
            }
        })
    }

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

    private fun vaiParaFormulario() {
        val action =
            ListaPecaRoupaFragmentDirections.actionListaPecasRoupasToFormularioDelivery()
        navControler.navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelEstado.temComponentes = ComponetesVisuais(appBar = true, bottomNavigation = true)
        context?.let { configuraAdapter(view, it) }
        configuraSwipe()
        configuraDrawerLayout(view)
        configuraCliqueCardParaEdicao()
    }

    private fun configuraAdapter(view: View, context: Context) {
        recyclerView = view.findViewById(R.id.lista_roupas_recyclerview)
        adapter = ListaRoupasAdapter(context)
        recyclerView.adapter = adapter
    }

    private fun configuraSwipe() {
        var itemTouchHelper = context?.let { PecaRoupaItemTouchCallback(it, adapter) }?.let {
            ItemTouchHelper(
                it
            )
        }
        itemTouchHelper?.attachToRecyclerView(recyclerView)

        configuraDelecaoPorSwipe()
        configuraTrocaPosicoesPecaRoupa()
    }

    fun configuraDelecaoPorSwipe() {
        adapter?.removePecaRoupa = { idPecaDelecao ->
            context?.let {
                AlertDialogUtils(it).exibirDialogPadrao(
                    getString(R.string.titulo_dialog_cancelar_processo),
                    getString(R.string.mensagem_dialog_cancelar_processo),
                    object : AlertDialogUtils.CallBackDialog {
                        override fun cliqueBotaoConfirma() {
                            if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
                                cliqueConfirmaDelecao()
                            } else {
                                adapter?.notifyDataSetChanged()
                                ToastUtils().showCenterToastShort(
                                    context,
                                    getString(R.string.mensagen_conectese_a_rede)
                                )
                            }
                        }

                        private fun cliqueConfirmaDelecao() {
                            val spinner = ProgressBarUtils.mostraProgressBar(it)
                            viewModelPecasRoupa.deleta(idPecaDelecao)
                                .observe(
                                    viewLifecycleOwner, Observer {
                                        spinner.dismiss()
                                        if (it.dados ?: 0 > 0) {
                                            ToastUtils().showCenterToastShort(
                                                context,
                                                getString(R.string.toast_cancelado)
                                            )
                                        } else {
                                            it.erro?.let { it1 ->
                                                ToastUtils().showCenterToastShort(
                                                    context,
                                                    it1
                                                )
                                            }
                                            adapter?.notifyDataSetChanged()
                                        }

                                    }
                                )
                        }

                        override fun cliqueBotaoCancela() {
                            adapter?.notifyDataSetChanged()
                            ToastUtils().showCenterToastShort(
                                context,
                                getString(R.string.toast_acao_revertida)
                            )

                        }
                    })
            }
        }

    }

    fun configuraTrocaPosicoesPecaRoupa() {

        adapter?.trocaPosicoesPecasRoupa = { pecasRoupaParaEdicao ->

            viewModelPecasRoupa.trocaPosicoes(pecasRoupaParaEdicao)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (it.dados ?: 0 > 1 && it.erro == null) {
                        adapter?.notifyDataSetChanged()
                    } else {
                        it.erro?.let { it1 -> ToastUtils().showCenterToastShort(context, it1) }
                        adapter?.notifyDataSetChanged()
                    }
                }
                )
        }

    }


    private fun configuraDrawerLayout(view: View) {
        navigationView = view.findViewById(R.id.lista_roupas_nav_view)
        drawerLayout = view.findViewById(R.id.activity_lista_roupas_drawer)
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

    fun configuraCliqueCardParaEdicao() {
        adapter?.cliqueCardParaEdicao = { pecaRoupaEdicao ->
            val actionVaiParaFormulario =
                ListaPecaRoupaFragmentDirections.actionListaPecasRoupasToFormularioDelivery(
                    pecaRoupaEdicao
                )
            navControler.navigate(actionVaiParaFormulario)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout_header_lista_roupas -> {
                context?.let {
                    AlertDialogUtils(it).exibirDialogPadrao(getString(R.string.titulo_logout),
                        getString(R.string.mensagem_logout),
                        object : AlertDialogUtils.CallBackDialog {
                            override fun cliqueBotaoConfirma() {
                                deletaTokenAoConfirmarLogout()
                            }

                            private fun deletaTokenAoConfirmarLogout() {
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

                            override fun cliqueBotaoCancela() {}

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