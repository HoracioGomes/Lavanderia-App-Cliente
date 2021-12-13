package com.example.lavanderia_cliente.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Cliente::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCliente"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    )
)
class Token(
    @SerializedName("token")
    @Expose
    @PrimaryKey()
    var token: String,
    @SerializedName("idUsuario")
    @Expose
    var idCliente: Long
): Serializable