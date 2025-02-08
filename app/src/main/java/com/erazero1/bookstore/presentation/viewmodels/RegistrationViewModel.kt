package com.erazero1.bookstore.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erazero1.bookstore.model.Cart
import com.erazero1.bookstore.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.getReference(USERS_REFERENCE_PATH)

    val error = MutableLiveData<String>()
    val user = MutableLiveData<FirebaseUser>()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let {
                user.value = it
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                firebaseUser?.let {
                    val newUser = User(
                        id = it.uid,
                        email = email,
                        name = name,
                        profilePicture = "",
                        favorites = emptyMap(),
                        cart = Cart(id=it.uid)
                    )
                    reference.child(it.uid).setValue(newUser)
                }
            }
            .addOnFailureListener { e ->
                error.value = e.message
            }
    }

    companion object {
        private const val USERS_REFERENCE_PATH = "Users"
    }
}
