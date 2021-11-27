package com.example.lavanderia_cliente.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Cliente(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var nome: String?,

)