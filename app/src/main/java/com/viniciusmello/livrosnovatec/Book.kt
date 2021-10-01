package com.viniciusmello.livrosnovatec

data class Book(
    var categoria:String = "",
    var titulo: String = "",
    var autor: String = "",
    var ano: Int = 0,
    var paginas: Int = 0,
    var imageCapaUrl: String = ""
) {

    override fun toString(): String = titulo

}
