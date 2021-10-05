package com.viniciusmello.livrosnovatec.entities

import com.google.gson.annotations.SerializedName

data class Book(
    var categoria: String = "",
    var titulo: String = "",
    var autor: String = "",
    var ano: Int = 0,
    var paginas: Int = 0,
    @SerializedName("capa")
    var imageCapaUrl: String = ""
) {

    override fun toString(): String = titulo

}
