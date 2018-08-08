package com.example.hipolito.app_android_osm.infra.api.endpoints

import com.example.hipolito.app_android_osm.models.TokenAPIModel
import com.example.hipolito.app_android_osm.models.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginEndPoint{

    @POST("token/")
    fun logarAPI(@Body usuario: Usuario): Call<TokenAPIModel>

}