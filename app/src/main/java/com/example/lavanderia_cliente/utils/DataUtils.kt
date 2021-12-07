package com.example.lavanderia_cliente.utils

import java.text.SimpleDateFormat
import java.util.*

class DataUtils {
    fun dataAtualParaBanco(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val data = Date()
        return sdf.format(data)
    }
}