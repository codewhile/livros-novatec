package com.viniciusmello.livrosnovatec

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("categoria")
    val nome:String,
    val livros: List<Book>) { }