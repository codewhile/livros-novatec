package com.viniciusmello.livrosnovatec

import android.content.Context
import android.net.ConnectivityManager
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.TEXT
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object BookFinder {

    private const val SERVER_JSON =
        "https://raw.githubusercontent.com/nglauber/dominando_android3/master/livros_novatec.json"

    private const val SERVER_XML =
        "https://raw.githubusercontent.com/nglauber/dominando_android3/master/livros_novatec.xml"

    private const val TIME_SECONDS: Int = 1000

    fun loadBooksFromServerJson(): List<Book>? {

        val connection = getConnectionFromServer(SERVER_JSON)

        return if (connection.responseCode == HttpURLConnection.HTTP_OK) {

            val json = JSONObject(parseInputStreamToStringJson(connection.inputStream))
            getListBookFromJson(json)

        } else {

            null

        }

    }

    fun loadBooksFromServerXML(): List<Book>? {

        val connection = getConnectionFromServer(SERVER_XML)

        val books = mutableListOf<Book>()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {

            val inputStream = connection.inputStream
            books.addAll(getBooksFromXML(inputStream))

        }

        return books
    }

    private fun getBooksFromXML(inputStream: InputStream?): MutableList<Book> {

        val factory = XmlPullParserFactory.newInstance()

        val parser = factory.newPullParser()

        parser.setInput(inputStream, "UTF-8")

        var tag: String = ""
        var book: Book = Book()
        val books = mutableListOf<Book>()
        var currentCategory = ""
        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {

            when (eventType) {

                XmlPullParser.START_TAG -> {
                    tag = parser.name

                    if (tag == "livro") {
                        book = Book()
                        book.categoria = currentCategory
                    }


                }

                XmlPullParser.END_TAG -> {
                    tag = parser.name

                    if (tag == "livro") {
                        books.add(book)
                    }

                }

                TEXT -> {

                    var text = parser.text

                    if (!parser.isWhitespace) {
                        when (tag) {
                            "ano" -> book.ano = text.toInt()
                            "autor" -> book.autor = text
                            "capa" -> book.imageCapaUrl = text
                            "paginas" -> book.paginas = text.toInt()
                            "titulo" -> book.titulo = text
                            "categoria" -> currentCategory = text
                        }
                    }


                }


            }

            eventType = parser.next()

        }

        return books

    }

    private fun getListBookFromJson(jsonObject: JSONObject): List<Book> {

        val books = mutableListOf<Book>()

        val firstLevelArray: JSONArray =
            jsonObject.getJSONArray("novatec")

        var categoria: String

        for (index in 0 until firstLevelArray.length()) {

            var firstLevelObject = firstLevelArray.getJSONObject(index)
            categoria = firstLevelObject.getString("categoria")
            var secondLevelArray: JSONArray = firstLevelObject.getJSONArray("livros")

            for (index in 0 until secondLevelArray.length()) {
                val book = Book()

                var secondLevelObject = secondLevelArray.getJSONObject(index)
                book.titulo = secondLevelObject.getString("titulo")
                book.autor = secondLevelObject.getString("autor")
                book.ano = secondLevelObject.getInt("ano")
                book.paginas = secondLevelObject.getInt("paginas")
                book.imageCapaUrl = secondLevelObject.getString("capa")
                book.categoria = categoria
                books.add(book)
            }

        }

        return books

    }


    private fun getConnectionFromServer(path: String): HttpURLConnection {

        val url = URL(path)

        val connection = (url.openConnection() as HttpURLConnection).apply {
            this.requestMethod = "GET"
            this.readTimeout = 20 * TIME_SECONDS
            this.connectTimeout = 20 * TIME_SECONDS
            doInput = true
            doOutput = false
        }

        connection.connect()

        return connection

    }

    private fun parseInputStreamToStringJson(inputStream: InputStream): String {

        val byteArray = ByteArray(1024)

        val byteArrayOutPutStreamBuffer = ByteArrayOutputStream()

        var quantityBytesReaded: Int

        while (true) {
            quantityBytesReaded = inputStream.read(byteArray)
            if (quantityBytesReaded == -1) break
            byteArrayOutPutStreamBuffer.write(byteArray, 0, quantityBytesReaded)
        }


        return String(byteArrayOutPutStreamBuffer.toByteArray())

    }

    fun isDeviceConnected(context:Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

        return connectivityManager.activeNetworkInfo?.isConnected ?: false

    }



}