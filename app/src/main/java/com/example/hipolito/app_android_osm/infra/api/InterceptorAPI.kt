package com.example.hipolito.app_android_osm.infra.api

import okhttp3.Interceptor
import okhttp3.Response

class InterceptorAPI ( var TOKEN: String ) : Interceptor {

    val AUTHORIZATION = "Authorization"

    override fun intercept(chain: Interceptor.Chain?): Response? {

        val request = chain!!.request().newBuilder()
                .addHeader(AUTHORIZATION, TOKEN)
                .build()

        return chain.proceed(request)
    }
}