package com.erazero1.bookstore.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.erazero1.bookstore.data.local.room.BooksDatabase
import com.erazero1.bookstore.data.repository.FavoriteRepository
import com.erazero1.bookstore.model.Book
import com.erazero1.bookstore.model.FavoriteBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var repository: FavoriteRepository
    lateinit var favorites: LiveData<List<FavoriteBook>>

    init {
        try {
            val dao = BooksDatabase.getDatabase(application).favoriteBookDao()
            repository = FavoriteRepository(dao, application)
            favorites = repository.getAllFavorites()
        } catch (e: Exception) {
            Log.d("FavoriteViewModel", e.message!!)
        }
    }


    fun addFavorite(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavorite(book)
        }
    }

    fun removeFavorite(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavorite(book)
        }
    }
}
