package com.example.lavanderia_cliente.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.lavanderia_cliente.model.Token

@Dao
abstract class TokenDao {
    @Insert
    abstract fun salva(token: Token)

    @Query("SELECT * from token WHERE idCliente = :idCliente")
    abstract fun buscaToken(idCliente: Long?): Token

    @Delete
    abstract fun delete(token: Token): Int
}