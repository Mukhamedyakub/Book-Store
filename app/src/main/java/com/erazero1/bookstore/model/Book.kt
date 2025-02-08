package com.erazero1.bookstore.model

data class Book(
    val id: String = "",
    val title: String = "",
    val author: List<String> = arrayListOf(),
    val year: Int = 0,
    val language: String = "",
    val genre: String = "",
    val pages: String = "",
    val price: String = "",
    val currencyCode: String = "",
    val publishedDate: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val quantity: Int = 0
    )


