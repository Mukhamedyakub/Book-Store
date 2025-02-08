package com.erazero1.bookstore.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erazero1.bookstore.model.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _overallPrice = MutableLiveData<Double>()
    val overallPrice: LiveData<Double> get() = _overallPrice

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private var cartListener: ValueEventListener? = null

    init {
        fetchCartData()
    }

    private fun fetchCartData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _errorMessage.value = "User not logged in."
            return
        }
        val cartRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser.uid)
            .child("cart")

        cartListener = cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val overallPriceValue =
                    snapshot.child("overallPrice").getValue(Double::class.java) ?: 0.0
                _overallPrice.value = overallPriceValue

                val booksList = mutableListOf<Book>()
                val booksSnapshot = snapshot.child("books")
                for (bookSnapshot in booksSnapshot.children) {
                    val book = bookSnapshot.getValue(Book::class.java)
                    book?.let { booksList.add(it) }
                }
                _books.value = booksList
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
            }
        })
    }

    fun deleteBookFromCart(book: Book) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _errorMessage.value = "User not logged in."
            return
        }
        if (book.id.isEmpty()) {
            _errorMessage.value = "Book id is empty."
            return
        }

        val userCartRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser.uid)
            .child("cart")

        userCartRef.child("books").child(book.id)
            .removeValue()
            .addOnSuccessListener {
                updateOverallPrice(-book.price.toDouble())
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }

    private fun updateOverallPrice(delta: Double) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val overallPriceRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser.uid)
            .child("cart")
            .child("overallPrice")

        overallPriceRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val currentPrice = mutableData.getValue(Double::class.java) ?: 0.0
                val newPrice = (currentPrice + delta).coerceAtLeast(0.0)
                mutableData.value = newPrice
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    _errorMessage.postValue(error.message)
                }
            }
        })
    }

    fun buyCart() {
        // TODO: Implement cart purchase logic (e.g. updating Firebase, navigating to a checkout screen, etc.)
    }

    override fun onCleared() {
        super.onCleared()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null && cartListener != null) {
            FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.uid)
                .child("cart")
                .removeEventListener(cartListener!!)
        }
    }
}
