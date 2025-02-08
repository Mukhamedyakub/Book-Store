package com.erazero1.bookstore.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.erazero1.bookstore.R
import com.erazero1.bookstore.data.repository.BooksRepository
import com.erazero1.bookstore.data.retrofit.RetrofitInstance
import com.erazero1.bookstore.model.Book
import com.erazero1.bookstore.presentation.viewmodels.BookDetailsViewModel
import com.erazero1.bookstore.presentation.viewmodels.BookDetailsViewModelFactory
import com.erazero1.bookstore.presentation.viewmodels.FavoriteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: BookDetailsViewModel
    private lateinit var bookId: String
    private var book = Book()
    private lateinit var imageViewCover: ImageView
    private lateinit var textViewTitle: TextView
    private lateinit var textViewAuthors: TextView
    private lateinit var textViewPublishedDate: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var fabAddToCart: FloatingActionButton
    private lateinit var buttonBookmark: ImageButton
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_details)

        val apiService = RetrofitInstance.api
        val booksRepository = BooksRepository(apiService)
        viewModel = ViewModelProvider(
            this, BookDetailsViewModelFactory(booksRepository)
        )[BookDetailsViewModel::class.java]
        favoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FavoriteViewModel::class.java]

        bookId = intent.getStringExtra(EXTRA_BOOK_ID).toString()
        viewModel.getBookById(bookId)

        viewModel.book.observe(this) { fetchedBook ->
            book = fetchedBook
            refreshViews()
        }

        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        imageViewCover = findViewById(R.id.imageViewCover)
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewAuthors = findViewById(R.id.textViewAuthors)
        textViewPublishedDate = findViewById(R.id.textViewPublishedDate)
        textViewPrice = findViewById(R.id.textViewPrice)
        textViewDescription = findViewById(R.id.textViewDescription)
        fabAddToCart = findViewById(R.id.fabAddToCart)
        buttonBookmark = findViewById(R.id.buttonBookmark)
    }

    private fun initListeners() {
        fabAddToCart.setOnClickListener {
            viewModel.addBookToCart(book)
        }

        buttonBookmark.setOnClickListener {
            val isFavorite = favoriteViewModel.favorites.value?.any { it.id == book.id } ?: false
            if (isFavorite) {
                favoriteViewModel.removeFavorite(book)
                buttonBookmark.setImageResource(R.drawable.baseline_bookmark_24)
            } else {
                favoriteViewModel.addFavorite(book)
                buttonBookmark.setImageResource(R.drawable.baseline_bookmark_border_24_non_filled)
            }
        }

    }

    private fun initObservers() {
        viewModel.addToCartSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Book added to cart!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Price is not available", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }

        favoriteViewModel.favorites.observe(this) { favorites ->
            val isFavorite = favorites.any { it.id == bookId }
            if (isFavorite) {
                buttonBookmark.setImageResource(R.drawable.baseline_bookmark_24)
            } else {
                buttonBookmark.setImageResource(R.drawable.baseline_bookmark_border_24_non_filled)
            }
        }

    }

    @SuppressLint("StringFormatInvalid")
    private fun refreshViews() {
        Glide.with(applicationContext)
            .load(book.coverUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageViewCover)
        textViewTitle.text = book.title
        textViewAuthors.text = getString(R.string.authors, book.author.toString())
        textViewPublishedDate.text = getString(R.string.publishedDate, book.publishedDate)
        textViewPrice.text = "${book.price} ${book.currencyCode}"
        textViewDescription.text = book.description
    }

    companion object {
        private const val EXTRA_BOOK_ID = "book_id"

        fun newIntent(context: Context, bookId: String): Intent {
            return Intent(context, BookDetailsActivity::class.java).apply {
                putExtra(EXTRA_BOOK_ID, bookId)
            }
        }

    }
}