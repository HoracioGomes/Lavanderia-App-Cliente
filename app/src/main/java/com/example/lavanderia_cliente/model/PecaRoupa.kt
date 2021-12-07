package com.example.lavanderia_cliente.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
//    (
//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = Cliente::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("idCliente"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//    )
//)
class PecaRoupa(
//    @PrimaryKey(autoGenerate = true)
    @PrimaryKey()
    @SerializedName("id")
    @Expose
    var id: Long = 0,
    @SerializedName("nome")
    @Expose
    var nome: String?,
    @SerializedName("status")
    @Expose
    var status: String?,
    @SerializedName("data")
    @Expose
    var data: String?,
    @SerializedName("posicaoNaLista")
    @Expose
    var posicaoNaLista: Long = 0
//    var idCliente: Int?
) : Serializable {
    fun formataData(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(this.data)
    }
}