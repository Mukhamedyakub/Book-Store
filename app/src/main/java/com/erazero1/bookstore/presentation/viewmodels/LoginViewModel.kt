package com.erazero1.bookstore.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val error = MutableLiveData<String>()
    private val user = MutableLiveData<FirebaseUser>()

    val userLiveData: LiveData<FirebaseUser> get() = user
    val errorLiveData: LiveData<String> get() = error
    private val _currentUserId = auth.currentUser?.uid
    val currentUserId: String get() = _currentUserId.toString()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let {
                user.value = it
            }
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("LoginViewModel", "Successful login")
            }
            .addOnFailureListener { exception ->
                error.value = exception.message
            }
    }
}
