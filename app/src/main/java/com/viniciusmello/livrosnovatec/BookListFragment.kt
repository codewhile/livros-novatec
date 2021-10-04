package com.viniciusmello.livrosnovatec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BookListFragment : Fragment(), CoroutineScope {

    //    private var async: AsynkBookFinder? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var job = Job()
    private var dowloadJob: Job? = null
    private lateinit var viewHolder: ViewHolder

    private val books = mutableListOf<Book>()

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

    override fun onDestroy() {
        super.onDestroy()
        dowloadJob?.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewHolder = ViewHolder(view)

        val arrayAdapter = BookAdapter(requireContext(), books)

        viewHolder.lisView.adapter = arrayAdapter

        hideProgress()

        if (BookFinder.isDeviceConnected(requireContext())) {

            if (dowloadJob == null) {

                if (books.isEmpty()) {

                    showProgress()
                    initDowloadJob()

                }
            } else {

                showProgress()

            }

        } else {
            viewHolder.message.text = getString(R.string.no_conected)
        }

    }

    private fun initDowloadJob() {

        dowloadJob = launch {
            // Executa função suspensa de forma Síncrona
            val books: List<Book>? = withContext(Dispatchers.IO) {
                BookFinder.loadBooksFromServerJson()
            }

            // Executa Threads Pararelamente
//            val bood: Deferred<List<Book>?> = async(Dispatchers.IO) {
//                BookFinder.loadBooksFromServerXML()
//            }
//
//            bood.await()

            if (books != null && books.isNotEmpty())
                updateUI(books)
        }


    }

//    private fun loadBooksFromServer() {
//        async?.execute()
//    }

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
            val adapter = viewHolder.lisView.adapter as BookAdapter
            adapter.notifyDataSetChanged()
            hideProgress()
            dowloadJob = null

        } else {

            viewHolder.message.text = getString(R.string.no_conected)
            viewHolder.message.visibility = View.VISIBLE

        }
    }


//    inner class AsynkBookFinder() :
//        AsyncTask<Void, Void, List<Book>?>() {
//
//        override fun onPreExecute() {
//            showProgress()
//        }
//
//        override fun doInBackground(vararg params: Void?): List<Book>? {
//            return BookFinder.loadBooksFromServerXML()
//        }
//        override fun onPostExecute(result: List<Book>?) {
//            hideProgress()
//            updateUI(result)
//        }
//    }

}