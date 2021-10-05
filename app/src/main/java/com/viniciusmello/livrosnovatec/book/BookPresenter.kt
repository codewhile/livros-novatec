package com.viniciusmello.livrosnovatec.book

class BookPresenter(
    private val model: BookModel,
    private val view: BookView
) {

    fun loadBooks() {

        view.showProgress(true)

        model.loadBooks { books ->

            view.showProgress(false)

            if (books.isNotEmpty()) {
                view.updateUI(books)
            }
            else {
                view.showMessageNoInternet()
            }

        }
    }

}