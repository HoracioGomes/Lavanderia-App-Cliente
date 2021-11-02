package com.example.lavanderia_cliente.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.PecaRoupa

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
        val peca: PecaRoupa = pecasRoupas[position]
        val nomePecaRoupa = holder.itemView.findViewById<TextView>(R.id.card_peca_roupa_nome_peca)
        val statusPecaRoupa =
            holder.itemView.findViewById<TextView>(R.id.card_peca_roupa_status_roupa)
        nomePecaRoupa.text = peca.nome
        statusPecaRoupa.text = peca.status
    }

    override fun getItemCount(): Int {
        return pecasRoupas.size
    }

    fun adicionaPecaRoupa(peca: PecaRoupa) {
        pecasRoupas.add(peca)
        notifyDataSetChanged()
    }

    class ListaRoupasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}