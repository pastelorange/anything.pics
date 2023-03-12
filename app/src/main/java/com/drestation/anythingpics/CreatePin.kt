package com.drestation.anythingpics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.drestation.anythingpics.databinding.ActivityCreatePinBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// This class handles writing to Firebase
class CreatePin : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePinBinding
    private var imageUrl: String? = null // Global var to pass between functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button to open device image gallery
        binding.imgUploadBtn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imageUploadLauncher.launch(galleryIntent)
        }

        binding.createPinBtn.setOnClickListener {
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

                uploadTask.addOnSuccessListener {
                    storageRef.child("img/$imageFileName").downloadUrl.addOnSuccessListener {
                        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                        imageUrl = it.toString()
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    private fun createPin() {
        val title = binding.editTitleTxt.text.toString()
        val caption = binding.editCaptionTxt.text.toString()

        if (title.isNotEmpty() && caption.isNotEmpty() && imageUrl != null) {
            // Get the current user
            val uid = Firebase.auth.currentUser!!.uid

            // Create Pin object
            val pin = Pin(title, caption, imageUrl, uid)

            // Connect to Firestore
            val db = FirebaseFirestore.getInstance().collection("pinboard")

            // Save as document
            val documentId = "$title-$uid"
            db.document(documentId).set(pin)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pin posted!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, PinboardActivity::class.java))
                }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
        }
    }
}