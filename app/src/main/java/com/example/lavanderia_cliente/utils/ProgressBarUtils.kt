package com.example.lavanderia_cliente.utils

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.lavanderia_cliente.R

class ProgressBarUtils {
    companion object {

        fun mostraProgressBar(context: Context): Dialog {

            var alertDialog = AlertDialog.Builder(context)
            alertDialog.setCancelable(false)
            alertDialog.setView(R.layout.layout_progress_bar)
            var dialog = alertDialog.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
            return dialog

        }

    }

}