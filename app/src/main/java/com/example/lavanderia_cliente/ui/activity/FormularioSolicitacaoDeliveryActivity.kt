package com.example.lavanderia_cliente.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.ui.fragment.FormularioDeliveryFragment
import com.example.lavanderia_cliente.utils.Constantes

class FormularioSolicitacaoDeliveryActivity : AppCompatActivity() {

    private val pecaRoupa by lazy {
        intent.getSerializableExtra(Constantes.EXTRA_PECA_PARA_EDICAO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_solicitacao_delivery_layout)
        title = getString(R.string.titulo_bar_formulario_solic_edicao)
        geraFragmentFormularioDelivery()
    }

    private fun geraFragmentFormularioDelivery() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        val formularioDeliveryFragment = FormularioDeliveryFragment()
        var dados = Bundle()
        dados.putSerializable(Constantes.EXTRA_PECA_PARA_EDICAO, pecaRoupa)
        formularioDeliveryFragment.arguments = dados
        beginTransaction.add(R.id.formulario_delivery_container, formularioDeliveryFragment)
        beginTransaction.commit()
    }


    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is FormularioDeliveryFragment) {
            fragment.quandoFinish = {
                finish()
            }

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}