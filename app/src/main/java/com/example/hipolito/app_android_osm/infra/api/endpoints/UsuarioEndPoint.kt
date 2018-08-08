package com.example.hipolito.app_android_osm.infra.api.endpoints

import com.example.hipolito.app_android_osm.model.Usuario
import retrofit2.Call
import retrofit2.http.GET

interface UsuarioEndPoint {

    @GET("usuario/")
    fun getUsuarioLogado(): Call<Usuario>

}