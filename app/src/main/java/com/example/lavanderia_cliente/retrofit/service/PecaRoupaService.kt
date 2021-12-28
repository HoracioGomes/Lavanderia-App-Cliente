package com.example.lavanderia_cliente.retrofit.service

import com.example.lavanderia_cliente.model.PecaRoupa
import retrofit2.Call
import retrofit2.http.*

interface PecaRoupaService {
    @GET("pecas-roupas/roupas")
    fun buscaTodasPecas(): Call<List<PecaRoupa>>

    @POST("pecas-roupas/roupas")
    fun salva(@Body pecaRoupa: PecaRoupa): Call<PecaRoupa>

    @PUT("pecas-roupas/roupas")
    fun edita(@Body pecaRoupa: PecaRoupa?): Call<PecaRoupa>

    @PUT("pecas-roupas/roupas/muda-posicao")
    fun trocaPosicao(@Body pecaRoupa: MutableList<PecaRoupa>?): Call<Void>

    @DELETE("pecas-roupas/roupas/{id}")
    fun delete(@Path("id") id: Long?) : Call<Void>
}