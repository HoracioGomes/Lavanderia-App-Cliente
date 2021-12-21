package com.example.lavanderia_cliente.ui.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.extensions.transacaoFragment
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.ui.fragment.FormularioDeliveryFragment
import com.example.lavanderia_cliente.ui.fragment.ListaPecaRoupaFragment
import com.example.lavanderia_cliente.utils.Constantes.Companion.TAG_FRAGMENT_FORMULARIO

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (intent.hasExtra(getString(R.string.extra_cliente_logado))) {
            ListaPecaRoupaFragment.cliente =
                intent.getSerializableExtra(getString(R.string.extra_cliente_logado)) as Cliente
        }

        if (intent.hasExtra(getString(R.string.extra_token_valido))) {
            ListaPecaRoupaFragment.token =
                intent.getSerializableExtra(getString(R.string.extra_token_valido)) as Token
        }

        if (savedInstanceState == null) {
            geraFragmentInicial()
        } else {
            supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_FORMULARIO)?.let { fragment ->

                val argumentos = fragment.arguments
                val novoFragmentFormulario = FormularioDeliveryFragment()
                novoFragmentFormulario.arguments = argumentos

                transacaoFragment {
                    remove(fragment)
                }

                supportFragmentManager.popBackStack()

                transacaoFragment {

                    val container =
//                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        if (findViewById<FrameLayout>(R.id.activity_main_container_secundario) != null) {
                            R.id.activity_main_container_secundario
                        } else {
                            R.id.activity_main_container_primario
                        }
                    replace(container, novoFragmentFormulario, TAG_FRAGMENT_FORMULARIO)
                }

                transacaoFragment {
                    if (findViewById<FrameLayout>(R.id.activity_main_container_secundario) != null) {
                        replace(R.id.activity_main_container_primario, ListaPecaRoupaFragment())
                    }
                }

            }

        }

    }

    private fun geraFragmentInicial() {

        transacaoFragment {
            replace(R.id.activity_main_container_primario, ListaPecaRoupaFragment())
        }

        supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_FORMULARIO)
            ?.let { fragmentFormulario ->
                transacaoFragment {
                    remove(fragmentFormulario)
                }
            }

    }

    private fun vaiParaFormulario() {
        transacaoFragment {

            val container =
//                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (findViewById<FrameLayout>(R.id.activity_main_container_secundario) != null) {
                    R.id.activity_main_container_secundario
                } else {
                    R.id.activity_main_container_primario
                }

            replace(container, FormularioDeliveryFragment(), TAG_FRAGMENT_FORMULARIO)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is ListaPecaRoupaFragment -> {
                fragment.quandoBtnFabClicado = this::vaiParaFormulario

            }

            is FormularioDeliveryFragment -> {
                fragment.quandoFinish = this::geraFragmentInicial
            }
        }
    }


    override fun onBackPressed() {
//        moveTaskToBack(true)
        geraFragmentInicial()
    }

}