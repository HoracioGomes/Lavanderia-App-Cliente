package com.example.lavanderia_cliente.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.asynctasks.BuscaPecasRoupaTask
import com.example.lavanderia_cliente.asynctasks.DeletaPecaRoupaTask
import com.example.lavanderia_cliente.asynctasks.EditaPecaRoupaTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.activity.FormularioSolicitacaoDeliveryActivity
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import java.util.*

class ListaRoupasAdapter(
    private val context: Context,
    private val pecaRoupaDao: PecaRoupaDao
) : RecyclerView.Adapter<ListaRoupasAdapter.ListaRoupasViewHolder>() {

    val pecasRoupas: MutableList<PecaRoupa>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaRoupasViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.card_peca_roupa_layout, parent, false)
        return ListaRoupasViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaRoupasViewHolder, position: Int) {
        holder.vincula(pecasRoupas?.get(position))
        holder.itemView.setOnClickListener {
            var editaNota = Intent(context, FormularioSolicitacaoDeliveryActivity::class.java)

            editaNota.putExtra(
                EXTRA_PECA_PARA_EDICAO,
                pecasRoupas?.get(holder.adapterPosition)
            )

            context.startActivity(editaNota)
        }
    }

    override fun getItemCount(): Int {
        if (pecasRoupas != null) {
            return pecasRoupas.size
        }

        return 0
    }

    fun atualiza() {
        pecasRoupas?.clear()
        BuscaPecasRoupaTask(pecaRoupaDao, object : BuscaPecasRoupaTask.ListenerBuscaPecasRoupa {
            override fun retorno(pecasRoupasRetornadas: MutableList<PecaRoupa>?) {
                if (pecasRoupasRetornadas != null) {
                    pecasRoupas?.addAll(pecasRoupasRetornadas)
                    pecasRoupas?.sortBy { pecaRoupa -> pecaRoupa.posicaoNaLista }
                    notifyDataSetChanged()
                }
            }
        }).execute()
    }


    fun remove(position: Int) {
        DeletaPecaRoupaTask(pecaRoupaDao, pecasRoupas?.get(position),
            object : DeletaPecaRoupaTask.ListenerDeletaPecaRoupa {
                override fun deletada(nomePeca: String?) {
                    pecasRoupas?.removeAt(position)
                    notifyItemRemoved(position)
                }

            }).execute()
    }

    fun troca(posicaoInicial: Int, posicaoFinal: Int) {
        var pos1 = pecasRoupas?.get(posicaoInicial)?.posicaoNaLista
        var pos2 = pecasRoupas?.get(posicaoFinal)?.posicaoNaLista
        if (pos2 != null) {
            pecasRoupas?.get(posicaoInicial)?.posicaoNaLista = pos2
        }
        if (pos1 != null) {
            pecasRoupas?.get(posicaoFinal)?.posicaoNaLista = pos1
        }
        mutableListOf(pecasRoupas?.get(posicaoInicial), pecasRoupas?.get(posicaoFinal))?.let {
            EditaPecaRoupaTask(
                pecaRoupaDao,
                it,
                object : EditaPecaRoupaTask.ListenerEditaPecaRoupa {
                    override fun editado(nomePeca: String?) {}
                }).execute()
            Collections.swap(pecasRoupas, posicaoInicial, posicaoFinal)
            notifyItemMoved(posicaoInicial, posicaoFinal)
        }

    }

    class ListaRoupasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomePecaRoupa = itemView.findViewById<TextView>(R.id.card_peca_roupa_nome_peca)
        val statusPecaRoupa =
            itemView.findViewById<TextView>(R.id.card_peca_roupa_status_roupa)

        fun vincula(peca: PecaRoupa?) {
            nomePecaRoupa.text = peca?.nome
            statusPecaRoupa.text = peca?.status
        }
    }
}