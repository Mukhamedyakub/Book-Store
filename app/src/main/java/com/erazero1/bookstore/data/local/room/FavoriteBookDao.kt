package com.erazero1.bookstore.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erazero1.bookstore.model.FavoriteBook

@Dao
interface FavoriteBookDao {
    @Query("SELECT * FROM favorite_books")
    fun getAllFavoriteBooks(): LiveData<List<FavoriteBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(book: FavoriteBook)

    @Delete
    suspend fun deleteFavorite(book: FavoriteBook)
}
