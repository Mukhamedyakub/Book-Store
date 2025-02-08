package com.erazero1.bookstore.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erazero1.bookstore.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData
    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> get() = _user

    private var userListener: ValueEventListener? = null

    init {
        val userId = auth.currentUser?.uid

        auth.addAuthStateListener {
            _user.value = auth.currentUser
        }
        if (userId != null) {

             userListener = dbRef.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let { _userLiveData.value = it }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ProfileViewModel", error.message)
                }
            })
        }
    }

    fun logout() {
        auth.signOut()
    }
    fun updateProfilePicture(newPicBase64: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            dbRef.child(userId).child("profilePicture").setValue(newPicBase64)
        }
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.let {
            auth.currentUser?.uid?.let { uid ->
                dbRef.child(uid).removeEventListener(it)
            }
        }
    }
}
