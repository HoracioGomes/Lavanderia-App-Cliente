package com.example.lavanderia_cliente.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = Cliente::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("idCliente"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//
//        )
//    )
)
class Telefone(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var numero: String?,
    var idCliente: Int?
)
