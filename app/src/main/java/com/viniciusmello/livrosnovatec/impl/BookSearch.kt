package com.viniciusmello.livrosnovatec.impl

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.viniciusmello.livrosnovatec.entities.Book
import com.viniciusmello.livrosnovatec.book.BookModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BookSearch(val ctx: Context) :
    BookModel, CoroutineScope {

    private val job: Job = Job()
    private var downloadJob: Job? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun loadBooks(callback: (List<Book>) -> Unit) {

        val connectivityManager = ctx.getSystemService(Service.CONNECTIVITY_SERVICE) as
                ConnectivityManager

        val monitor = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                downloadJob(callback)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                callback(emptyList())
            }

            override fun onUnavailable() {
                super.onUnavailable()
                callback(emptyList())
            }

        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
            .build()

        connectivityManager.registerNetworkCallback(request, monitor)


    }

    private fun downloadJob(callback: (List<Book>) -> Unit) {

        downloadJob = launch {
            var books: List<Book> = emptyList()

            books = withContext(Dispatchers.IO) {
                BookFinder.getBooksByOkHttp3AndGson()
            }

            callback(books)

            job.cancel()
        }
    }

}