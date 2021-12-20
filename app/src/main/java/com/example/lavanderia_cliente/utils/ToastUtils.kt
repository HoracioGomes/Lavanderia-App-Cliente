package com.example.lavanderia_cliente.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.example.lavanderia_cliente.R

class ToastUtils {

    fun showCenterToastShort(context: Context?, message: String) {
        var toast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
    fun showCenterToastLong(context: Context, message: String) {
        var toast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}