package com.example.lavanderia_cliente.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
class Cliente(
    @PrimaryKey()
    @SerializedName("id")
    @Expose
    var id: Long,
    @SerializedName("nome")
    @Expose
    var nome: String?,
    @SerializedName("email")
    @Expose
    var email: String?

): Serializable