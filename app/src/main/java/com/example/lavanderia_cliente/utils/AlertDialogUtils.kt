package com.example.lavanderia_cliente.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialogUtils(val context: Context) {
    fun exibirDialogPadrao(titulo: String, mensagem: String, callBack: CallBackDialog) {
        MaterialAlertDialogBuilder(context)
//            .setView()
            .setCancelable(false)
            .setMessage(mensagem)
            .setTitle(titulo)
            .setPositiveButton("Sim") { dialog, which ->
                dialog.dismiss()
                callBack.cliqueBotaoConfirma()
            }
            .setNegativeButton("Não") { dialog, which ->
                dialog.dismiss()
                callBack.cliqueBotaoCancela()
            }
            .show()

    }

    interface CallBackDialog {
        fun cliqueBotaoConfirma()
        fun cliqueBotaoCancela()
    }
}