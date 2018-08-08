package com.example.hipolito.app_android_osm.infra.api.endpoints

import com.example.hipolito.app_android_osm.model.MensagemResponse
import com.example.hipolito.app_android_osm.model.Refeicao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CardapioEndPoint {

    @GET("refeicao/{data}/")
    fun getRefeicoes(@Path("data") data: String): Call<MutableList<Refeicao>>

    @GET("refeicoes/{id}/interesse/")
    fun confirmarInteresse(@Path("id") id: Long): Call<MensagemResponse>

}