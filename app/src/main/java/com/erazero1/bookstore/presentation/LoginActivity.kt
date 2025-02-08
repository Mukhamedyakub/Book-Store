package com.erazero1.bookstore.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.erazero1.bookstore.R
import com.erazero1.bookstore.presentation.viewmodels.LoginViewModel
import com.google.firebase.FirebaseApp

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var textViewRegister: TextView
    private lateinit var textViewForgotPassword: TextView
    private lateinit var buttonLogin: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        initViews()
        initListeners()
        observeViewModel()
    }

    private fun initViews() {
        textViewRegister = findViewById(R.id.textViewRegister)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
    }

    private fun initListeners() {
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            if (email == "" || password == "") {
                Toast.makeText(this, "Email or password are invalid", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }

        }

        textViewForgotPassword.setOnClickListener {
            val intent = ResetPasswordActivity.newIntent(
                this@LoginActivity,
                editTextEmail.text.toString().trim()
            )
            startActivity(intent)
        }

        textViewRegister.setOnClickListener {
            val intent = RegistrationActivity.newIntent(this@LoginActivity)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.errorLiveData.observe(this) { error ->
            error?.let {
                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userLiveData.observe(this) { firebaseUser ->
            firebaseUser?.let {
                val intent = HomeActivity.newIntent(this@LoginActivity, viewModel.userLiveData.value!!.uid)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
