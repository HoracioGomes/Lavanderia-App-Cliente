package com.example.lavanderia_cliente.ui.activity.callback

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter

class PecaRoupaItemTouchCallback(var context: Context, var adapter: ListaRoupasAdapter) :
    Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val direcoesDoacao = LEFT or RIGHT
        val direcoesMoveCard = LEFT or RIGHT or UP or DOWN
        return makeMovementFlags(direcoesMoveCard, direcoesDoacao)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.troca(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.remove(position)
        Toast.makeText(
            context, context.getString(R.string.msg_finalizacao_doacao),
            Toast.LENGTH_SHORT
        ).show()
    }

}
