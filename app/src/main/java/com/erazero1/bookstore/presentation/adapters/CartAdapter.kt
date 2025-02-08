package com.erazero1.bookstore.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.erazero1.bookstore.R
import com.erazero1.bookstore.model.Book

class CartAdapter(
    private var books: List<Book>,
    private val onDeleteClicked: (Book) -> Unit
) : RecyclerView.Adapter<CartAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewCover: ImageView = itemView.findViewById(R.id.imageViewCover)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val textViewYear: TextView = itemView.findViewById(R.id.textViewYear)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.textViewTitle.text = book.title
        holder.textViewAuthor.text = book.author.toString()
        holder.textViewYear.text = book.year.toString()
        holder.textViewPrice.text = "${book.currencyCode} ${book.price}"

        Glide.with(holder.itemView.context)
            .load(book.coverUrl)
            .placeholder(R.drawable.ic_image_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageViewCover)

        holder.buttonDelete.setOnClickListener {
            onDeleteClicked(book)
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
