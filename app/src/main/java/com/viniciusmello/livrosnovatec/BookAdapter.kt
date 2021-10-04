package com.viniciusmello.livrosnovatec

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class BookAdapter(context: Context, var books: MutableList<Book>) :
    ArrayAdapter<Book>(context, 0, books) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val book = books[position]

        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view =
                LayoutInflater.from(context)
                    .inflate(R.layout.books_view, parent, false)


            holder = ViewHolder(view)
            view.tag = holder

        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.name.text = book.titulo
        holder.category.text = book.categoria
        holder.year.text = book.ano.toString()
        holder.page.text = book.paginas.toString()
        holder.author.text = book.autor
        Glide.with(context).load(Uri.parse(book.imageCapaUrl)).into(holder.image)

        return view

    }

    class ViewHolder(view: View) {
        var name: TextView = view.findViewById<TextView>(R.id.nomeDoLivro)
        var author: TextView = view.findViewById<TextView>(R.id.autorDoLivro)
        var page = view.findViewById<TextView>(R.id.paginaDoLivro)
        var year: TextView = view.findViewById<TextView>(R.id.anoDoLivro)
        var image = view.findViewById<ImageView>(R.id.imageView)
        var category: TextView = view.findViewById<TextView>(R.id.categoriaDoLivro)
    }


}