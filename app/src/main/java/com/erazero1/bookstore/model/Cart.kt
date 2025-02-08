package com.erazero1.bookstore.model

data class Cart(
    val id: String = "",
    val books: Map<String, Book> = emptyMap(),
    val overallPrice: Double = 0.0,
)