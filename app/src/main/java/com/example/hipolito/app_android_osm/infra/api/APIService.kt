package com.example.hipolito.app_android_osm.infra.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIService {

    private val BASE_URL = "http://192.168.43.128:8000/api/v1/"

    private lateinit var retrofit: Retrofit
    private lateinit var interceptorAPI: InterceptorAPI

    constructor(Token: String) {

        interceptorAPI = InterceptorAPI("Token " + Token)

        val builderUsuario = OkHttpClient.Builder()
        builderUsuario.addInterceptor(interceptorAPI)
        val usuario = builderUsuario.build()

        val builderRetrofit = Retrofit.Builder()
        retrofit = builderRetrofit
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(usuario)
                .build()
    }
}