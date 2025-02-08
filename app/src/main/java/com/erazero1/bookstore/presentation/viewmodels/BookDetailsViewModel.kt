package com.erazero1.bookstore.presentation.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.erazero1.bookstore.model.Book
import com.erazero1.bookstore.data.repository.BooksRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class BookDetailsViewModel(private val booksRepository: BooksRepository) : ViewModel() {

    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book> get() = _book

    private val _addToCartSuccess = MutableLiveData<Boolean>()
    val addToCartSuccess: LiveData<Boolean> get() = _addToCartSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun addBookToCart(book: Book) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _errorMessage.value = "Please sign in first."
            return
        }

        val overallPriceRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser.uid)
            .child("cart")
            .child("overallPrice")

        overallPriceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val overallPrice = snapshot.getValue(Double::class.java) ?: 0.0
                addBookToTheCart(currentUser, book, overallPrice, overallPriceRef)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching overallPrice: ${error.message}")
            }
        })

    }

    private fun addBookToTheCart(
        currentUser: FirebaseUser,
        book: Book,
        overallPrice: Double,
        overallPriceRef: DatabaseReference
    ) {
        if (book.price == "" || book.price == "0.0") {
            _addToCartSuccess.value = false
            return
        }
        val price = book.price.toDouble() + overallPrice


        val bookRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser.uid)
            .child("cart")
            .child("books")
        overallPriceRef.setValue(price)
            .addOnSuccessListener {
                Log.d("BookDetailsViewModel", "price is added")
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }

        bookRef.child(book.id).setValue(book)
            .addOnSuccessListener {
                _addToCartSuccess.value = true
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }


    fun getBookById(bookId: String) {
        viewModelScope.launch {
            val book = booksRepository.getBookById(bookId)
            _book.postValue(book)
        }
    }
}
