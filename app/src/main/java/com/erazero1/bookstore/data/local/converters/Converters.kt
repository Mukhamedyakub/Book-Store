package com.erazero1.bookstore.data.local.converters

import com.erazero1.bookstore.model.Book
import com.erazero1.bookstore.model.FavoriteBook

fun Book.toFavoriteBook(): FavoriteBook {
    return FavoriteBook(
        id = id,
        title = title,
        author = author.toString(),
        year = year,
        price = price.toDouble(),
        currency = currencyCode,
        cover = coverUrl
    )
}

fun FavoriteBook.toBook(): Book {
    return Book(
        id = id,
        title = title,
        author = listOf(author),
        year = year,
        price = price.toString(),
        currencyCode = currency,
        coverUrl = cover
    )
}
