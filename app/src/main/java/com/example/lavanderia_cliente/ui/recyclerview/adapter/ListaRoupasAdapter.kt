package com.example.lavanderia_cliente.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.ui.activity.FormularioSolicitacaoDeliveryActivity
import com.example.lavanderia_cliente.utils.AlertDialogUtils
import com.example.lavanderia_cliente.utils.ConnectionManagerUtils
import com.example.lavanderia_cliente.utils.Constantes.Companion.EXTRA_PECA_PARA_EDICAO
import com.example.lavanderia_cliente.utils.ProgressBarUtils
import com.example.lavanderia_cliente.utils.ToastUtils
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
        if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
            pecasRoupas?.clear()
            RepositoryPecaRoupa(context).buscaPecasRoupa(object :
                RepositoryPecaRoupa.CallBackRepositorypecaRoupa<MutableList<PecaRoupa>> {
                override fun quandoSucesso(dados: MutableList<PecaRoupa>) {
                    pecasRoupas?.addAll(dados)
                    pecasRoupas?.sortBy { pecaRoupa -> pecaRoupa.posicaoNaLista }
                    notifyDataSetChanged()
                }

                override fun quandoFalha(erro: String) {
                    Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
                }
            })
        } else {

            ToastUtils().showCenterToastShort(
                context,
                context.getString(R.string.mensagen_conectese_a_rede)
            )

        }
    }


    fun remove(position: Int) {
        AlertDialogUtils(context).exibirDialogPadrao(
            context.getString(R.string.titulo_dialog_cancelar_processo),
            context.getString(R.string.mensagem_dialog_cancelar_processo),
            object : AlertDialogUtils.CallBackDialog {
                override fun cliqueBotaoConfirma() {
                    if (ConnectionManagerUtils().checkInternetConnection(context) == 1) {
                        RepositoryPecaRoupa(context).deletaPecaRoupa(pecasRoupas?.get(position)?.id,
                            object : RepositoryPecaRoupa.CallBackRepositorypecaRoupaSemBody {
                                override fun quandoSucesso() {
                                    mostraSpinner()
                                    atualiza()
                                    ToastUtils().showCenterToastShort(
                                        context,
                                        context.getString(R.string.toast_cancelado)
                                    )
                                }

                                override fun quandoFalha(erro: String) {
                                    ToastUtils().showCenterToastShort(
                                        context,
                                        "Falha ao cancelar!\n$erro"
                                    )
                                    atualiza()
                                }
                            })
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

    private fun mostraSpinner() {
        val spinner = ProgressBarUtils.mostraProgressBar(context)
        val handler = Handler()
        handler.postDelayed({
            spinner.dismiss()
        }, 500)
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


            var pecasParaEdicao: MutableList<PecaRoupa?> =
                mutableListOf(pecasRoupas?.get(posicaoInicial), pecasRoupas?.get(posicaoFinal))

            RepositoryPecaRoupa(context).trocaPosicao(
                pecasParaEdicao,
                object : RepositoryPecaRoupa.CallBackRepositorypecaRoupaSemBody {
                    override fun quandoSucesso() {

                        Collections.swap(pecasRoupas, posicaoInicial, posicaoFinal)
                        notifyDataSetChanged()
                    }

                    override fun quandoFalha(erro: String) {
                    }

                })
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