package com.erazero1.bookstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_books")
data class FavoriteBook(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val year: Int,
    val price: Double,
    val currency: String,
    val cover: String
)
