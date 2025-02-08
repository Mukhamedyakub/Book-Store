package com.erazero1.bookstore.data.retrofit

import com.erazero1.bookstore.data.entity.BookItem
import com.erazero1.bookstore.data.entity.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") volumeId: String,
        @Query("key") apiKey: String
    ): BookItem

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("country") country: String
    ): BookResponse

    @GET("volumes")
    suspend fun getBooksBySearch(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("country") country: String
    ): BookResponse
}