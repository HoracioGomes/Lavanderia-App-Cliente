package com.example.lavanderia_cliente.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.database.converters.ConversorCalendar
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.PecaRoupa
import com.example.lavanderia_cliente.model.Telefone

@Database(
    entities = [
        Cliente::class,
        PecaRoupa::class,
        Telefone::class
    ],
    version = 8,
    exportSchema = false

)
@TypeConverters(ConversorCalendar::class)
abstract class LavanderiaDatabase : RoomDatabase() {

    abstract fun getPecaRoupaDao(): PecaRoupaDao

    companion object {

        fun getAppDatabase(context: Context?): LavanderiaDatabase {

            return Room.databaseBuilder(
                context!!,
                LavanderiaDatabase::class.java,
                context.getString(R.string.nome_db)
            )
//                .addMigrations(
//                    *LavanderiaMigrations().MIGRATIONS
//                )
                .fallbackToDestructiveMigration()
                .build()
        }

    }

}