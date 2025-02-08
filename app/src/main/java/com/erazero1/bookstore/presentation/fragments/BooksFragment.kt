package com.erazero1.bookstore.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erazero1.bookstore.R
import com.erazero1.bookstore.data.repository.BooksRepository
import com.erazero1.bookstore.data.retrofit.RetrofitInstance
import com.erazero1.bookstore.model.Book
import com.erazero1.bookstore.presentation.BookDetailsActivity
import com.erazero1.bookstore.presentation.adapters.BooksAdapter
import com.erazero1.bookstore.presentation.viewmodels.BooksViewModel
import com.erazero1.bookstore.presentation.viewmodels.BooksViewModelFactory
import com.erazero1.bookstore.presentation.viewmodels.FavoriteViewModel

class BooksFragment : Fragment(R.layout.fragment_books) {
    private lateinit var viewModel: BooksViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BooksAdapter
    private lateinit var textViewNotFound: TextView
    private lateinit var progressBar: ProgressBar


    private var books: List<Book> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitInstance.api
        val repository = BooksRepository(apiService)

        viewModel = ViewModelProvider(this, BooksViewModelFactory(repository))[BooksViewModel::class.java]
        favoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FavoriteViewModel::class.java]
        initViews(view)
        adapter = BooksAdapter(books, { bookId ->
            val intent = BookDetailsActivity.newIntent(view.context, bookId)
            startActivity(intent)
        }, { isEmpty ->
            textViewNotFound.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }, { currentBook, imageButtonFavorite ->
            val isFavorite = favoriteViewModel.favorites.value?.any { it.id == currentBook.id } ?: false
            if (isFavorite) {
                favoriteViewModel.removeFavorite(currentBook)
            } else {
                favoriteViewModel.addFavorite(currentBook)
            }
        })


        recyclerView.adapter = adapter

        initObservers()

        viewModel.fetchBooks()

        val editTextSearch = view.findViewById<EditText>(R.id.editTextSearch)
        val buttonSearch = view.findViewById<ImageButton>(R.id.buttonSearch)
        buttonSearch.setOnClickListener {
            val searchQuery = editTextSearch.text.toString().trim()
            if (searchQuery.isNotEmpty()) {
                progressBar.visibility = ProgressBar.VISIBLE
                viewModel.searchBooks(searchQuery)
            }
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = editTextSearch.text.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    viewModel.searchBooks(searchQuery)
                }
                true
            } else {
                false
            }
        }
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        textViewNotFound = view.findViewById(R.id.textViewNotFound)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
    }

    private fun initObservers() {
        viewModel.books.observe(viewLifecycleOwner) { booksList ->
            Log.d("BooksFragment", booksList.toString())
            books = booksList
            progressBar.visibility = ProgressBar.GONE
            adapter.updateBooks(books)
        }

        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favorites ->

            val favoritesIds = favorites.map { it.id }.toSet()
            adapter.updateFavoritesIds(favoritesIds)
        }

    }
}
