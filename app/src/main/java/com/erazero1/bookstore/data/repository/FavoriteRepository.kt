package com.erazero1.bookstore.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.erazero1.bookstore.data.local.converters.toFavoriteBook
import com.erazero1.bookstore.data.local.room.FavoriteBookDao
import com.erazero1.bookstore.model.Book
import com.erazero1.bookstore.model.FavoriteBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavoriteRepository(
    private val favoriteBookDao: FavoriteBookDao,
    private val context: Context
) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDBRef = FirebaseDatabase.getInstance().getReference("Users")

    suspend fun addFavorite(book: Book) {
        favoriteBookDao.insertFavorite(book.toFavoriteBook())
        firebaseAuth.currentUser?.let { user ->
            firebaseDBRef.child(user.uid)
                .child("favorites")
                .child(book.id)
                .setValue(book)
        }
    }

    suspend fun removeFavorite(book: Book) {
        favoriteBookDao.deleteFavorite(book.toFavoriteBook())
        firebaseAuth.currentUser?.let { user ->
            firebaseDBRef.child(user.uid)
                .child("favorites")
                .child(book.id)
                .removeValue()
        }
    }

    fun getAllFavorites(): LiveData<List<FavoriteBook>> {
        return favoriteBookDao.getAllFavoriteBooks()
    }
}
