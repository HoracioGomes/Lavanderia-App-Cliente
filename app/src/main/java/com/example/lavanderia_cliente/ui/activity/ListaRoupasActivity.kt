package com.example.lavanderia_cliente.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.ui.fragment.ListaPecaRoupaFragment

class ListaRoupasActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.titulo_bar_lista_roupas))
        setContentView(R.layout.activity_lista_roupas_layout)
        if (intent.hasExtra(getString(R.string.extra_cliente_logado))) {
            ListaPecaRoupaFragment.cliente =
                intent.getSerializableExtra(getString(R.string.extra_cliente_logado)) as Cliente
        }

        if (intent.hasExtra(getString(R.string.extra_token_valido))) {
            ListaPecaRoupaFragment.token =
                intent.getSerializableExtra(getString(R.string.extra_token_valido)) as Token
        }

    }

    private fun vaiParaFormulario() {
        val vaiParaFormularioDelivery =
            Intent(this, FormularioSolicitacaoDeliveryActivity::class.java)
        startActivity(
            vaiParaFormularioDelivery
        )

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is ListaPecaRoupaFragment) {
            fragment.quandoBtnFabClicado = {
                vaiParaFormulario()
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}