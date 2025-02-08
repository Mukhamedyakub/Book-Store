package com.erazero1.bookstore.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erazero1.bookstore.data.repository.BooksRepository
import com.erazero1.bookstore.model.Book
import kotlinx.coroutines.launch

class BooksViewModel(private val repository: BooksRepository) : ViewModel() {

    val books = MutableLiveData<List<Book>>()

    fun fetchBooks() {
        viewModelScope.launch {
            val fetchedBooks = repository.getBooks()
            books.value = fetchedBooks
        }
    }

    fun searchBooks(title: String) {
        viewModelScope.launch {
            val searchResults = repository.getBooksBySearch(title)
            books.value = searchResults
        }
    }
}
