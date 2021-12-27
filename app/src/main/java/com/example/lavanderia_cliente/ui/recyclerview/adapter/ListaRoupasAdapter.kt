package com.example.lavanderia_cliente.ui.recyclerview.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.ui.activity.MainActivity
import com.example.lavanderia_cliente.ui.fragment.FormularioDeliveryFragment
import com.example.lavanderia_cliente.ui.fragment.ListaPecaRoupaFragmentDirections
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.utils.*
import com.example.lavanderia_cliente.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import java.util.*

class ListaRoupasAdapter(
    private val context: Context,
    private val viewModelRoupa: PecaRoupaViewModel,
    private val navController: NavController
) : RecyclerView.Adapter<ListaRoupasAdapter.ListaRoupasViewHolder>() {

    val pecasRoupas: MutableList<PecaRoupa> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaRoupasViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.card_peca_roupa_layout, parent, false)
        return ListaRoupasViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaRoupasViewHolder, position: Int) {
        holder.vincula(pecasRoupas?.get(position))
        holder.itemView.setOnClickListener {
            vaiParaEdicaoNoFormulario(holder)
        }
    }

    private fun vaiParaEdicaoNoFormulario(holder: ListaRoupasViewHolder) {
//        val formularioDeliveryFragment = FormularioDeliveryFragment()
//        val dados = Bundle()
//        dados.putSerializable(
//            EXTRA_PECA_PARA_EDICAO,
//            pecasRoupas?.get(holder.adapterPosition)
//        )
//        formularioDeliveryFragment.arguments = dados

        if (context is MainActivity) {
            val mainActivity = context

//            val container =
//                if (mainActivity.findViewById<FrameLayout>(R.id.activity_main_container_secundario) != null) {
//                    R.id.activity_main_container_secundario
//                } else {
//                    R.id.activity_main_container_primario
//                }
            val pecaParaEdicao = pecasRoupas[holder.adapterPosition]
            val actionListaPecasRoupasToFormularioDeliveryComPecaEdicao =
                ListaPecaRoupaFragmentDirections.actionListaPecasRoupasToFormularioDelivery(
                    pecaParaEdicao
                )
            navController.navigate(actionListaPecasRoupasToFormularioDeliveryComPecaEdicao)


//            val transaction = fragmentManager?.beginTransaction()
//
//            transaction?.replace(
//                container,
//                formularioDeliveryFragment,
//                Constantes.TAG_FRAGMENT_FORMULARIO
//            )
//            transaction?.commit()
        }
    }

    override fun getItemCount(): Int {
        if (pecasRoupas != null) {
            return pecasRoupas.size
        }

        return 0
    }

    fun atualiza() {

        viewModelRoupa.buscaTodos()
            .observe(context as LifecycleOwner, androidx.lifecycle.Observer {
                it.dados?.let { dados ->
                    pecasRoupas?.clear()
                    pecasRoupas?.addAll(dados)
                    pecasRoupas?.sortBy { pecaRoupa -> pecaRoupa.posicaoNaLista }
                    notifyDataSetChanged()
                }
                it.erro?.let { erro ->
                    ToastUtils().showCenterToastLong(context, "$erro")
                }
            })

    }


    fun remove(position: Int) {
        AlertDialogUtils(context).exibirDialogPadrao(
            context.getString(R.string.titulo_dialog_cancelar_processo),
            context.getString(R.string.mensagem_dialog_cancelar_processo),
            object : AlertDialogUtils.CallBackDialog {
                override fun cliqueBotaoConfirma() {
                    if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
                        val spinner = ProgressBarUtils.mostraProgressBar(context)

                        viewModelRoupa.deleta(pecasRoupas?.get(position)?.id)
                            .observe(
                                context as LifecycleOwner, androidx.lifecycle.Observer {

                                    if (it.erro == null) {
                                        spinner.dismiss()
                                        ToastUtils().showCenterToastShort(
                                            context,
                                            context.getString(R.string.toast_cancelado)
                                        )
                                    } else {
                                        spinner.dismiss()
                                        ToastUtils().showCenterToastShort(context, it.erro)
                                        notifyDataSetChanged()
                                    }

                                }
                            )

                    } else {
                        ToastUtils().showCenterToastShort(
                            context,
                            context.getString(R.string.mensagen_conectese_a_rede)
                        )
                    }
                }

                override fun cliqueBotaoCancela() {
                    ToastUtils().showCenterToastShort(
                        context,
                        context.getString(R.string.toast_acao_revertida)
                    )
                    atualiza()
                }
            })
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


            viewModelRoupa.trocaPosicoes(pecasParaEdicao)
                .observe(context as LifecycleOwner, androidx.lifecycle.Observer {
                    if (it.erro == null) {
                        Collections.swap(pecasRoupas, posicaoInicial, posicaoFinal)
                        notifyDataSetChanged()
                    } else {
                        ToastUtils().showCenterToastShort(context, it.erro)
                        notifyDataSetChanged()
                    }
                }
                )
        } else {
            ToastUtils().showCenterToastShort(
                context,
                context.getString(R.string.mensagen_conectese_a_rede)
            )
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