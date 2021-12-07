package com.example.lavanderia_cliente.retrofit.service

import com.example.lavanderia_cliente.model.PecaRoupa
import retrofit2.Call
import retrofit2.http.*

interface PecaRoupaService {
    @GET("roupas")
    fun buscaTodasPecas(): Call<MutableList<PecaRoupa>>

    @POST("roupas")
    fun salva(@Body pecaRoupa: PecaRoupa): Call<PecaRoupa>

    @PUT("roupas")
    fun edita(@Body pecaRoupa: PecaRoupa): Call<PecaRoupa>

    @PUT("roupas/muda-posicao")
    fun trocaPosicao(@Body pecaRoupa: MutableList<PecaRoupa?>): Call<Void>

    @DELETE("roupas/{id}")
    fun delete(@Path("id") id: Long?) : Call<Void>
}