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

class BooksAdapter(
    private var books: List<Book>,
    private val onItemClick: (String) -> Unit,
    private val onListIsEmpty: (Boolean) -> Unit,
    private val onFavoriteIconClick: (Book, ImageButton) -> Unit
) : RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {

    // Cache the IDs of the favorite books
    private var favoriteIds: Set<String> = emptySet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BooksViewHolder(view)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = books[position]
        Glide.with(holder.itemView.context)
            .load(book.coverUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_image_placeholder)
            .into(holder.imageViewPoster)

        holder.textViewTitle.text = book.title
        holder.textViewAuthor.text = book.author.toString()

        if (favoriteIds.contains(book.id)) {
            holder.imageButtonFavorite.setImageResource(R.drawable.baseline_bookmark_24)
        } else {
            holder.imageButtonFavorite.setImageResource(R.drawable.baseline_bookmark_border_24_non_filled)
        }

        holder.imageButtonFavorite.setOnClickListener {
            onFavoriteIconClick(book, holder.imageButtonFavorite)
        }

        holder.itemView.setOnTouchListener { view, event ->
            val overlay = view.findViewById<View>(R.id.viewPosterOverlay)
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    overlay?.alpha = 0.4f
                }

                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    overlay?.alpha = 0f
                }
            }
            false
        }
        holder.itemView.setOnClickListener {
            onItemClick(book.id)
        }

    }

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        onListIsEmpty(books.isEmpty())
        notifyDataSetChanged()
    }

    fun updateFavoritesIds(newFavoriteIds: Set<String>) {
        favoriteIds = newFavoriteIds
        notifyDataSetChanged()
    }

    class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewPoster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val imageButtonFavorite: ImageButton = itemView.findViewById(R.id.imageButtonFavorite)
        val viewPosterOverlay: View = itemView.findViewById(R.id.viewPosterOverlay)
    }
}




