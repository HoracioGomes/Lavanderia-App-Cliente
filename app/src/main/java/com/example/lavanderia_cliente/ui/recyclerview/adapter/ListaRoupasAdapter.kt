package com.example.lavanderia_cliente.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.activity.FormularioSolicitacaoDeliveryActivity
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import com.example.lavanderia_cliente.ui.utils.Constantes.Companion.EXTRA_POSICAO_PECA_PARA_EDICAO
import java.util.*

class ListaRoupasAdapter(
    private val context: Context,
    private val pecasRoupas: MutableList<PecaRoupa>
) : RecyclerView.Adapter<ListaRoupasAdapter.ListaRoupasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaRoupasViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.card_peca_roupa_layout, parent, false)
        return ListaRoupasViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaRoupasViewHolder, position: Int) {
        holder.vincula(pecasRoupas[position])
        holder.itemView.setOnClickListener {
            var editaNota = Intent(context, FormularioSolicitacaoDeliveryActivity::class.java)
            editaNota.putExtra(
                EXTRA_PECA_PARA_EDICAO,
                pecasRoupas[position]
            )
            editaNota.putExtra(
                EXTRA_POSICAO_PECA_PARA_EDICAO,
                holder.adapterPosition
            )
            context.startActivity(editaNota)
        }
    }

    override fun getItemCount(): Int {
        return pecasRoupas.size
    }

    fun adicionaPecaRoupa(peca: PecaRoupa) {
        pecasRoupas.add(peca)
        notifyDataSetChanged()
    }

    fun alteraPecaRoupa(posicao: Int, pecaRoupa: PecaRoupa) {
        pecasRoupas.set(posicao, pecaRoupa)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        pecasRoupas.removeAt(position)
        notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun troca(posicaoInicial: Int, posicaoFinal: Int) {
        Collections.swap(pecasRoupas, posicaoInicial, posicaoFinal)
        notifyDataSetChanged()
        notifyItemMoved(posicaoInicial, posicaoFinal)
    }

    class ListaRoupasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomePecaRoupa = itemView.findViewById<TextView>(R.id.card_peca_roupa_nome_peca)
        val statusPecaRoupa =
            itemView.findViewById<TextView>(R.id.card_peca_roupa_status_roupa)

        fun vincula(peca: PecaRoupa) {
            nomePecaRoupa.text = peca.nome
            statusPecaRoupa.text = peca.status
        }
    }
}