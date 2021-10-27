package com.example.lavanderia_cliente

import android.app.Application
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa

class LavanderiaApplication : Application() {
    lateinit var dao: PecaRoupaDao

    override fun onCreate() {
        super.onCreate()
        dao = PecaRoupaDao()
        inserePecasExemplo()
    }

    private fun inserePecasExemplo() {
        var peca_1 = PecaRoupa(nome = "camiseta", status = "secagem")
        var peca_2 = PecaRoupa(nome = "bermuda", status = "lavagem")
        var peca_3 = PecaRoupa(nome = "casaco", status = "lavagem")
        var peca_4 = PecaRoupa(nome = "camiseta", status = "pronto para retirada")
        dao.insere(peca_1, peca_2, peca_3, peca_4)
    }

}