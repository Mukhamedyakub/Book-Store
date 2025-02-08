package com.erazero1.bookstore.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erazero1.bookstore.data.repository.BooksRepository

class BookDetailsViewModelFactory(
    private val booksRepository: BooksRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailsViewModel(booksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
