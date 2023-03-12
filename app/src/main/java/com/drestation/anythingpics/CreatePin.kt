package com.drestation.anythingpics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
    private var imageFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgUploadBtn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imageUploadLauncher.launch(galleryIntent)
        }

        binding.createPinBtn.setOnClickListener {
            val title = binding.editTitleTxt.text.toString()
            val caption = binding.editCaptionTxt.text.toString()

            if (title.isNotEmpty() && caption.isNotEmpty() && imageFileName != null) {
                // Get the current user
                val uid = Firebase.auth.currentUser!!.uid

                // Create Pin object
                val pin = Pin(title, caption, imageFileName, uid)

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

    private var imageUploadLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // Get uri of selected image
                val imageUri: Uri? = result.data?.data

                // Extract file name with extension
                imageFileName =
                    imageUri?.path?.lastIndexOf('/')?.let { imageUri.path?.substring(it) }

                // Uploading the file
                val storageRef = Firebase.storage.reference
                val uploadTask = storageRef.child("img/$imageFileName").putFile(imageUri!!)

                uploadTask.addOnSuccessListener {
                    storageRef.child("upload/$imageFileName").downloadUrl.addOnSuccessListener {
                        Toast.makeText(this, "New pin added!", Toast.LENGTH_LONG).show()
                    }
                }
//                    }.addOnFailureListener {
//                        Toast.makeText(this, "DOWNLOAD FAIL", Toast.LENGTH_LONG).show()
//                        Log.e("Firebase", "Failed in downloading")
//                    }
//                }.addOnFailureListener {
//                    Toast.makeText(this, "UPLOAD FAIL", Toast.LENGTH_LONG).show()
//                    Log.e("Firebase", "Image Upload fail")
//                }
            }
        }
}