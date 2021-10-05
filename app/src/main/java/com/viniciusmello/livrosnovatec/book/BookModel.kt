package com.viniciusmello.livrosnovatec.book

import com.viniciusmello.livrosnovatec.entities.Book

interface BookModel {

    fun loadBooks(callback: (List<Book>) -> Unit)


}