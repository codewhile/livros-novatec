package com.viniciusmello.livrosnovatec

import com.google.gson.annotations.SerializedName

data class Livraria(

    @SerializedName("novatec")
    val categorias: List<Categoria> = emptyList()) {


}