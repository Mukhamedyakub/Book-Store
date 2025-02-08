package com.erazero1.bookstore.data.repository

import android.util.Log
import com.erazero1.bookstore.BuildConfig
import com.erazero1.bookstore.data.entity.BookItem
import com.erazero1.bookstore.data.mapper.BookItemMapper
import com.erazero1.bookstore.data.mapper.Mapper
import com.erazero1.bookstore.data.retrofit.GoogleBooksApi
import com.erazero1.bookstore.model.Book
import kotlin.random.Random

class BooksRepository(
    private val apiService: GoogleBooksApi,
    private val bookItemMapper: Mapper<BookItem, Book> = BookItemMapper()
) {
    private val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY
    private val country = "RU"
    private val queryList = arrayListOf(
        "Android", "IOS", "Google", "Facebook", "Linux", "Python", "Java", "Kotlin"
    )

    suspend fun getBooks(): List<Book> {
        return try {
            val response = apiService.getBooks(queryList[Random.Default.nextInt(queryList.size)], apiKey, country)
            response.items?.map { bookItemMapper.map(it) } ?: emptyList()
        } catch (e: Exception) {
            Log.e("BooksRepository", e.message.toString())
            emptyList()
        }
    }

    suspend fun getBookById(id: String): Book {
        return try {
            val response = apiService.getBookById(id, apiKey)
            bookItemMapper.map(response)
        } catch (e: Exception) {
            Log.e("BooksRepository", e.message.toString())
            Book()
        }
    }


    suspend fun getBooksBySearch(query: String): List<Book> {
        return try {
            val response = apiService.getBooksBySearch(query, apiKey, country)
            response.items?.map { bookItemMapper.map(it) } ?: emptyList()
        } catch (e: Exception) {
            Log.e("BooksRepository", e.message.toString())
            emptyList()
        }
    }

}