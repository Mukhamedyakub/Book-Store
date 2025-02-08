package com.erazero1.bookstore.data.entity

data class BookResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo?
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String
)

data class SaleInfo(
    val country: String?,
    val saleability: String?,
    val isEbook: Boolean?,
    val listPrice: PriceInfo?,
    val retailPrice: PriceInfo?
)

data class PriceInfo(
    val amount: Double?,
    val currencyCode: String?
)
