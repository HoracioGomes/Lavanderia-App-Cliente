package com.example.lavanderia_cliente.ui.activity.callback

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.ToastUtils

class PecaRoupaItemTouchCallback(var context: Context, var adapter: ListaRoupasAdapter) :
    Callback() {
    private var dragFrom = -1
    private var dragTo = -1

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val direcoesDeletar = LEFT or RIGHT
        val direcoesMudarPosicao = LEFT or RIGHT or UP or DOWN
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            return makeMovementFlags(direcoesMudarPosicao, direcoesDeletar)
        } else {
            ToastUtils().showCenterToastShort(
                context,
                context.getString(R.string.mensagen_conexao_necessaria_acao)
            )
            return makeMovementFlags(0,0)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        dragTo = target.adapterPosition
        return true

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ACTION_STATE_DRAG -> {
                viewHolder?.also { dragFrom = it.adapterPosition }
            }

            ACTION_STATE_IDLE -> {
                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    adapter.trocaPosicaoNoAdapterEApi(dragFrom, dragTo)
                    dragFrom = -1
                    dragTo = -1
                }
            }
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        adapter.remove(position)

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

    }

}
