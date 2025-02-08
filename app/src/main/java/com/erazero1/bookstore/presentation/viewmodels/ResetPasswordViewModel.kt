package com.erazero1.bookstore.presentation.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val error: MutableLiveData<String> = MutableLiveData()
    private val success: MutableLiveData<Boolean> = MutableLiveData()

    fun getError(): MutableLiveData<String> = error

    fun isSuccess(): MutableLiveData<Boolean> = success

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                success.value = true
            }
            .addOnFailureListener { e ->
                error.value = e.message
            }
    }
}
