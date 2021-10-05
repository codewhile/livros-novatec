package com.viniciusmello.livrosnovatec.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.viniciusmello.livrosnovatec.BookAdapter
import com.viniciusmello.livrosnovatec.R
import com.viniciusmello.livrosnovatec.book.BookPresenter
import com.viniciusmello.livrosnovatec.book.BookView
import com.viniciusmello.livrosnovatec.entities.Book

class BookListFragment : Fragment(),
    BookView {

    private val books = mutableListOf<Book>()

    private lateinit var  presenter: BookPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter =
            BookPresenter(BookSearch(requireContext()), this)

        view.findViewById<ListView>(R.id.listView).adapter =
            BookAdapter(requireContext(), books)

        presenter.loadBooks()

    }

    override fun showProgress(show: Boolean) {
        view?.let { view ->
            view.findViewById<TextView>(R.id.txtMessage).visibility = isVisibleView(show)
            view.findViewById<ProgressBar>(R.id.progress).visibility = isVisibleView(show)
        }
    }

    override fun showMessageNoInternet() {
        Toast.makeText(requireContext(), getString(R.string.no_conected), Toast.LENGTH_SHORT).show()
    }

    override fun updateUI(result: List<Book>) {

        books.clear()

        books.addAll(result)

        val listView =
            view?.findViewById<ListView>(R.id.listView)

        val adapter = listView?.adapter
                as BookAdapter

        adapter.notifyDataSetChanged()

    }

    private fun isVisibleView(config: Boolean): Int =
        if (config) View.VISIBLE else View.GONE

}
