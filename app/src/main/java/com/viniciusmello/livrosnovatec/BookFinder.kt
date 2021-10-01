package com.viniciusmello.livrosnovatec

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object BookFinder {

    private const val SERVER =
        "https://raw.githubusercontent.com/nglauber/dominando_android3/master/livros_novatec.json"

    private const val TIME_SECONDS:Int = 1000

    fun loadBooksFromServer(context: Context): List<Book>? {

        val connection = getConnectionFromServer()

        return if (connection.responseCode == HttpURLConnection.HTTP_OK) {

            val json = JSONObject(parseInputStreamToStringJson(connection.inputStream))
            getListBookFromJson(json)

        } else {

            null

        }

    }

    private fun getListBookFromJson(jsonObject: JSONObject): List<Book> {

        val books = mutableListOf<Book>()

        val firstLevelArray: JSONArray =
            jsonObject.getJSONArray("novatec")

        var categoria:String

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


    private fun getConnectionFromServer(): HttpURLConnection {

        val url = URL(SERVER)

        val connection = (url.openConnection() as HttpURLConnection).apply {
            this.requestMethod = "GET"
            this.readTimeout = 10 * TIME_SECONDS
            this.connectTimeout = 15 * TIME_SECONDS
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


}