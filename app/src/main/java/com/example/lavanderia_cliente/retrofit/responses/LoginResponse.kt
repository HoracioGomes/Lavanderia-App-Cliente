package com.example.lavanderia_cliente.retrofit.responses

import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse(
    @SerializedName("usuario")
    @Expose
    var cliente: Cliente,
    @SerializedName("token")
    @Expose
    var token: Token,
)