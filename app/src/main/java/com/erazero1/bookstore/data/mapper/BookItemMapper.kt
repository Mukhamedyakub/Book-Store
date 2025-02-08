package com.erazero1.bookstore.data.mapper

import com.erazero1.bookstore.data.entity.BookItem
import com.erazero1.bookstore.model.Book

class BookItemMapper : Mapper<BookItem, Book> {
    override fun map(input: BookItem): Book {
        return Book(
            id = input.id,
            title = input.volumeInfo.title,
            author = input.volumeInfo.authors ?: emptyList(),
            publishedDate = input.volumeInfo.publishedDate ?: "",
            description = input.volumeInfo.description ?: "",
            pages = input.volumeInfo.pageCount?.toString() ?: "Unknown",
            coverUrl = input.volumeInfo.imageLinks?.thumbnail ?: "",
            price = input.saleInfo?.listPrice?.amount?.toString() ?: "0.0",
            currencyCode = input.saleInfo?.listPrice?.currencyCode ?: ""
        )
    }
}
