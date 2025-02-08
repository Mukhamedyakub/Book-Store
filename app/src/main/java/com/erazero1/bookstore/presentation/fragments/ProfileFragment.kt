package com.erazero1.bookstore.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erazero1.bookstore.R
import com.erazero1.bookstore.presentation.LoginActivity
import com.erazero1.bookstore.presentation.viewmodels.ProfileViewModel
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var viewModel: ProfileViewModel

    private lateinit var imageViewProfilePicture: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var buttonLogout: Button

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        initObservers(view)
        initImagePickerLauncher()
        initListeners()
    }

    private fun initViews(view: View) {
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture)
        textViewName = view.findViewById(R.id.textViewName)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        buttonLogout = view.findViewById(R.id.buttonLogout)
    }


    private fun initObservers(view: View) {
        viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            textViewName.text = user.name
            textViewEmail.text = user.email

            if (user.profilePicture.isNotEmpty()) {
                val bitmap = base64ToBitmap(user.profilePicture)
                bitmap?.let { imageViewProfilePicture.setImageBitmap(it) }
            }
        }

        viewModel.user.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser == null) {
                val intent = LoginActivity.newIntent(view.context)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun initImagePickerLauncher() {
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uri
                        )
                        imageViewProfilePicture.setImageBitmap(bitmap)
                        val base64String = bitmapToBase64(bitmap)
                        viewModel.updateProfilePicture(base64String)
                    }
                }
            }
    }

    private fun initListeners() {
        imageViewProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        buttonLogout.setOnClickListener {
            viewModel.logout()
        }
    }


    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}
