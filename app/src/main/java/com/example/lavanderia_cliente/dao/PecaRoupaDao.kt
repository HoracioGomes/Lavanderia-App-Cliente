package com.example.lavanderia_cliente.dao

import com.example.lavanderia_cliente.model.PecaRoupa
import java.util.*

class PecaRoupaDao {

    companion object {
        val pecasRoupas: MutableList<PecaRoupa> = mutableListOf()
    }

    fun todas(): MutableList<PecaRoupa> {
        return pecasRoupas
    }

    fun insere(vararg peca: PecaRoupa) {
        pecasRoupas.addAll(peca)
    }

    fun altera(posicao: Int, peca: PecaRoupa) {
        pecasRoupas[posicao] = peca
    }

    fun remove(posicao: Int) {
        pecasRoupas.removeAt(posicao)
    }

    fun troca(posicaoInicio: Int, posicaoFim: Int) {
        Collections.swap(pecasRoupas, posicaoInicio, posicaoFim)
    }

    fun removeTodos() {
        pecasRoupas.clear()
    }
}