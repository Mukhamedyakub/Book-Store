package com.erazero1.bookstore.data.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}
