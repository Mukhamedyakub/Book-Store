package com.erazero1.bookstore.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erazero1.bookstore.R
import com.erazero1.bookstore.presentation.viewmodels.ResetPasswordViewModel
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels

class ResetPasswordActivity : AppCompatActivity() {

    private val viewModel: ResetPasswordViewModel by viewModels()
    private lateinit var editTextEmail: EditText
    private lateinit var buttonResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        observeViewModel()
        initViews()
        initListeners()

        val email = intent.getStringExtra(EXTRA_EMAIL)
        editTextEmail.setText(email)
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.editTextEmailReset)
        buttonResetPassword = findViewById(R.id.buttonResetPassword)
    }

    private fun initListeners() {
        buttonResetPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            if (email == "") {
                Toast.makeText(this, "Email are invalid", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.resetPassword(email)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isSuccess().observe(this) { success ->
            success?.let {
                if (it) {
                    Toast.makeText(this, R.string.reset_link_sent, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getError().observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val EXTRA_EMAIL = "email"

        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }
}