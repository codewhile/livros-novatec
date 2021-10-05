package com.viniciusmello.livrosnovatec.entities

import com.google.gson.annotations.SerializedName

data class Livraria(

    @SerializedName("novatec")
    val categorias: List<Categoria> = emptyList()) {


}