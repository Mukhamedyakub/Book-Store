package com.erazero1.bookstore.presentation

import com.erazero1.bookstore.presentation.viewmodels.RegistrationViewModel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.erazero1.bookstore.R

class RegistrationActivity : AppCompatActivity() {

    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextName: EditText
    private lateinit var buttonSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        observeViewModel()
        initViews()
        initListeners()
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextName = findViewById(R.id.editTextName)
        buttonSignUp = findViewById(R.id.buttonSignUp)
    }

    private fun initListeners() {
        buttonSignUp.setOnClickListener {
            val email = getTrimmedValue(editTextEmail)
            val password = getTrimmedValue(editTextPassword)
            val name = getTrimmedValue(editTextName)

            if (email == "" || password == "" || name == "") {
                Toast.makeText(this, "Invalid value/values", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.signUp(email, password, name)
            }

        }
    }

    private fun observeViewModel() {
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.user.observe(this) { firebaseUser ->
            firebaseUser?.let {
                val intent = HomeActivity.newIntent(this, viewModel.user.value!!.uid)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getTrimmedValue(editText: EditText): String {
        return editText.text.toString().trim()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegistrationActivity::class.java)
        }
    }
}
