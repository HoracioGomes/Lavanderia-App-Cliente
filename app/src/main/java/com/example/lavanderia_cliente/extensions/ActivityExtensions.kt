package com.example.lavanderia_cliente.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.transacaoFragment(executa: FragmentTransaction.() -> Unit) {
    val transacao = supportFragmentManager.beginTransaction()
    executa(transacao)
    transacao.commit()
}