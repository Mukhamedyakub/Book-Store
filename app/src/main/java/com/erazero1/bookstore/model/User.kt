package com.erazero1.bookstore.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val profilePicture: String = "",
    val favorites: Map<String, Book> = emptyMap(),
    val cart: Cart = Cart()
)
