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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CreatePin : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePinBinding // Enable binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBtn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imageUploadLauncher.launch(galleryIntent)
        }
    }

    private var imageUploadLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // Get uri of selected image
                val imageUri: Uri? = result.data?.data

                // Extract file name with extension
                val fileName = imageUri?.path?.lastIndexOf('/')?.let { imageUri.path?.substring(it) }

                // Uploading the file
                val storageRef = Firebase.storage.reference
                val uploadTask = storageRef.child("img/$fileName").putFile(imageUri!!)

                uploadTask.addOnSuccessListener {
                    storageRef.child("upload/$fileName").downloadUrl.addOnSuccessListener {
                        Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "DOWNLOAD FAIL", Toast.LENGTH_LONG).show()
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "UPLOAD FAIL", Toast.LENGTH_LONG).show()
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    private fun getFileName(uri: Uri): String? {
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}