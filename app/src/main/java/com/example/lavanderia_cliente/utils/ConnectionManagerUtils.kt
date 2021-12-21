package com.example.lavanderia_cliente.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class ConnectionManagerUtils {
    var TAG = "LOG_CM"
    var NETWORK_STATUS_NOT_CONNECTED = 0
    var NETWORK_STATUS_CONNECTED = 1

    fun checkInternetConnection(context: Context?): Int {
        val cm =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if ( cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isAvailable
            && cm.activeNetworkInfo!!.isConnected
        ) {
            NETWORK_STATUS_CONNECTED

        } else {
            NETWORK_STATUS_NOT_CONNECTED
        }
    }
}