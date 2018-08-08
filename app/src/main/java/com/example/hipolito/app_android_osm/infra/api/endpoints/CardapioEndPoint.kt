package com.example.hipolito.app_android_osm.infra.api.endpoints

import com.example.hipolito.app_android_osm.model.Refeicao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CardapioEndPoint {

    @GET("refeicoes/{data}/")
    fun getRefeicoes(@Path("data") data: String): Call<Refeicao>

}