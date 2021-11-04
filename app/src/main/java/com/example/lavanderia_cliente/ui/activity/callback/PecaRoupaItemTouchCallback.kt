package com.example.lavanderia_cliente.ui.activity.callback

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.dao.PecaRoupaDao
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter

class PecaRoupaItemTouchCallback(var context: Context, var adapter: ListaRoupasAdapter) :
    Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val direcoesDeslize = LEFT or RIGHT
        return makeMovementFlags(0, direcoesDeslize)

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        PecaRoupaDao().remove(position)
        adapter.remove(position)
        Toast.makeText(
            context, context.getString(R.string.msg_finalizacao_doacao),
            Toast.LENGTH_SHORT
        ).show()
    }

}
