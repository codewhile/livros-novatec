package com.viniciusmello.livrosnovatec.book

import com.viniciusmello.livrosnovatec.entities.Book

interface BookView {

    fun updateUI(books: List<Book>)
    fun showProgress(show: Boolean)
    fun showMessageNoInternet()

}