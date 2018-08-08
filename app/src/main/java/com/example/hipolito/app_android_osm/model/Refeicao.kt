package com.example.hipolito.app_android_osm.model

import com.google.gson.annotations.SerializedName

class Refeicao(
        @SerializedName("tipo") var tipo:String,
        var prato: Prato,
        var interessados: MutableList<Long>
    ){
    var id: Long = 0
}