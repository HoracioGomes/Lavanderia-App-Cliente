package com.example.lavanderia_cliente.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.databinding.CardPecaRoupaLayoutBinding
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.ToastUtils
import java.util.*

class ListaRoupasAdapter(
    private val context: Context
) : RecyclerView.Adapter<ListaRoupasAdapter.ListaRoupasViewHolder>() {

    val pecasRoupas: MutableList<PecaRoupa> = mutableListOf()
    var cliqueCardParaEdicao: (pecaRoupaEdicao: PecaRoupa?) -> Unit = {}
    var removePecaRoupa: (idPecaExclusao: Long) -> Unit = {}
    var trocaPosicoesPecasRoupa: (idPecaExclusao: MutableList<PecaRoupa>?) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaRoupasViewHolder {
        var inflate =
            LayoutInflater.from(context)
        val viewDataBinding =
            DataBindingUtil.inflate<CardPecaRoupaLayoutBinding>(
                inflate,
                R.layout.card_peca_roupa_layout,
                parent,
                false
            )
        return ListaRoupasViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: ListaRoupasViewHolder, position: Int) {
        holder.vincula(pecasRoupas?.get(position))
        holder.itemView.setOnClickListener {
            cliqueCardParaEdicao(pecasRoupas?.get(position))
        }
    }

    override fun getItemCount(): Int {
        if (pecasRoupas != null) {
            return pecasRoupas.size
        }

        return 0
    }

    fun popula(lista: List<PecaRoupa>?) {
        notifyItemRangeRemoved(0, pecasRoupas.size)
        pecasRoupas?.clear()
        lista?.let { pecasRoupas.addAll(it) }
        pecasRoupas?.sortBy { pecaRoupa -> pecaRoupa.posicaoNaLista }
        notifyItemRangeInserted(0, pecasRoupas.size)
    }


    fun remove(position: Int) {
        removePecaRoupa(pecasRoupas?.get(position)?.id)
    }

    fun trocaPosicaoNoAdapterEApi(posicaoInicial: Int, posicaoFinal: Int) {
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            var pos1 = pecasRoupas?.get(posicaoInicial)?.posicaoNaLista
            var pos2 = pecasRoupas?.get(posicaoFinal)?.posicaoNaLista
            if (pos2 != null) {
                pecasRoupas?.get(posicaoInicial)?.posicaoNaLista = pos2
            }
            if (pos1 != null) {
                pecasRoupas?.get(posicaoFinal)?.posicaoNaLista = pos1
            }

            var pecasParaEdicao: MutableList<PecaRoupa> =
                mutableListOf(pecasRoupas[posicaoInicial], pecasRoupas[posicaoFinal])

            trocaPosicoesPecasRoupa(pecasParaEdicao)
            Collections.swap(pecasRoupas, posicaoInicial, posicaoFinal)

        } else {
            ToastUtils().showCenterToastShort(
                context,
                context.getString(R.string.mensagen_conectese_a_rede)
            )
        }
    }

    class ListaRoupasViewHolder(private var cardViewBinding: CardPecaRoupaLayoutBinding) :
        RecyclerView.ViewHolder(cardViewBinding.root) {

        init {
            cardViewBinding.clique = this
        }

        val statusPecaRoupa =
            itemView.findViewById<TextView>(R.id.card_peca_roupa_status_roupa)


        fun vincula(peca: PecaRoupa?) {
            cardViewBinding.pecaRoupa = peca
            statusPecaRoupa.text = peca?.status
        }

// Implementar Data Binding
//        fun cliqueNota() {
//            Log.e("TESTE", "CLICOU NOTA")
//        }
    }

}