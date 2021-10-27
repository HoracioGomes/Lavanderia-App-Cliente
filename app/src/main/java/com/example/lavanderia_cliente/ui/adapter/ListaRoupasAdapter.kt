package com.example.lavanderia_cliente.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.PecaRoupa

class ListaRoupasAdapter(
    private val context: Context,
    private val listaRopas: MutableList<PecaRoupa>
) : BaseAdapter() {

    override fun getCount(): Int {
        return listaRopas.size
    }

    override fun getItem(position: Int): Any {
        return listaRopas[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewCriada: View =
            LayoutInflater.from(context).inflate(R.layout.card_peca_roupa_layout, parent, false)

        val pecaRoupa = listaRopas[position]

        val nomeNoCard = viewCriada.findViewById<TextView>(R.id.card_peca_roupa_nome_peca)
        val statusNoCard = viewCriada.findViewById<TextView>(R.id.card_peca_roupa_status_roupa)

        nomeNoCard.text = pecaRoupa.nome
        statusNoCard.text = pecaRoupa.status

        return viewCriada
    }

}