package com.example.lavanderia_cliente.database.converters

import androidx.room.TypeConverter
import java.util.*

class ConversorCalendar {

    @TypeConverter
    fun toLong(valor: Calendar?): Long {
        if (valor != null) {
            return valor.timeInMillis
        }
        return 0
    }

    @TypeConverter
    fun toCalendar(valor: Long): Calendar {
        var calendar = Calendar.getInstance()
        if (valor != null) {
            calendar.timeInMillis = valor
        }
        return calendar
    }
}
