package com.example.lavanderia_cliente.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lavanderia_cliente.model.Cliente

@Dao
abstract class ClienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun salva(cliente: Cliente)

    @Query("SELECT * FROM cliente")
    abstract fun todos(): MutableList<Cliente>
}