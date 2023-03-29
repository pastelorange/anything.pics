package com.drestation.anythingpics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.drestation.anythingpics.databinding.ActivityCreatePinBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.*

// This class handles writing to Firebase
class CreatePinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePinBinding
    private var imageUrl: String? = null // Var to pass between functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button to open device image gallery
        binding.imageUploadButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imageUploadLauncher.launch(galleryIntent)
        }

        binding.submitButton.setOnClickListener {
            createPin()
        }
    }

    private var imageUploadLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // Get uri of selected image
                val imageUri: Uri? = result.data?.data

                val imageFileName =
                    imageUri?.path?.lastIndexOf('/')?.let { imageUri.path?.substring(it) }

                val storageRef = Firebase.storage.reference
                // Upload to Firebase Storage with putFile()
                val uploadTask = storageRef.child("img/$imageFileName").putFile(imageUri!!)

                // When image is done uploading
                uploadTask.addOnSuccessListener {
                    storageRef.child("img/$imageFileName").downloadUrl.addOnSuccessListener {
                        // Save the URL
                        imageUrl = it.toString()

                        // Load that URL into the image preview
                        Picasso.get().load(imageUrl).into(binding.imageView)
                    }
                }
            }
        }

    private fun createPin() {
        val title = binding.editTitleText.text.toString()
        val caption = binding.editCaptionText.text.toString()
        val timestamp = Timestamp.now()

        if (title.isNotEmpty() && caption.isNotEmpty() && imageUrl != null) {
            // Get the current user
            val uid = Firebase.auth.currentUser!!.uid

            // Create Pin object
            val pin = Pin(title, caption, imageUrl, uid, timestamp)

            // Connect to Firestore
            val db = FirebaseFirestore.getInstance().collection(uid)

            // Save as document and return to previous activity
            // The document name is auto generated
            db.document().set(pin)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pin posted!", Toast.LENGTH_SHORT).show()
                    finish()
                }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
        }
    }
}