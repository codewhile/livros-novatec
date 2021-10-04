package com.viniciusmello.livrosnovatec

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class BookListFragment : Fragment() {


    private var async: AsynkBookFinder? = null
    private lateinit var viewHolder: ViewHolder
    private val books = mutableListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewHolder = ViewHolder(view)

        val arrayAdapter = BookAdapter(requireContext(), books)

        viewHolder.lisView.adapter = arrayAdapter

        hideProgress()

        if (BookFinder.isDeviceConnected(requireContext())) {

            if (async == null) {

                if (books.isEmpty()) {

                    showProgress()
                    async = AsynkBookFinder()
                    loadBooksFromServer()

                }
            }
            else {

                showProgress()

            }

        } else {
            viewHolder.message.text = getString(R.string.no_conected)
        }

    }

    private fun loadBooksFromServer() {
        async?.execute()
    }

    private fun showProgress() {

        viewHolder.message.text = getString(R.string.search)
        viewHolder.message.visibility = View.VISIBLE
        viewHolder.progressBar.visibility = View.VISIBLE

    }

    private fun hideProgress() {

        viewHolder.message.visibility = View.GONE
        viewHolder.progressBar.visibility = View.GONE


    }

    class ViewHolder(view: View) {

        val lisView: ListView = view?.findViewById<ListView>(R.id.listView)
        val progressBar: ProgressBar = view?.findViewById<ProgressBar>(R.id.progress)
        val message: TextView = view?.findViewById<TextView>(R.id.txtMessage)

    }

    private fun updateUI(result: List<Book>?) {
        if (result != null && result.isNotEmpty()) {

            books.clear()
            books.addAll(result)
            val adapter = viewHolder.lisView.adapter as ArrayAdapter<String>
            adapter.notifyDataSetChanged()
            async = null

        } else {

            viewHolder.message.text = getString(R.string.no_conected)
            viewHolder.message.visibility = View.VISIBLE

        }
    }


    inner class AsynkBookFinder() :
        AsyncTask<Void, Void, List<Book>?>() {

        override fun onPreExecute() {
            showProgress()
        }

        override fun doInBackground(vararg params: Void?): List<Book>? {
            return BookFinder.loadBooksFromServerXML()
        }
        override fun onPostExecute(result: List<Book>?) {
            hideProgress()
            updateUI(result)
        }
    }

}