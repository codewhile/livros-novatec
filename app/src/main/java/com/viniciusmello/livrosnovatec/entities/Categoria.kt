package com.viniciusmello.livrosnovatec.entities

import com.google.gson.annotations.SerializedName
import com.viniciusmello.livrosnovatec.entities.Book

data class Categoria(
    @SerializedName("categoria")
    val nome:String,
    val livros: List<Book>) { }