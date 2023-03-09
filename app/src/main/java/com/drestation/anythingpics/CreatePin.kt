package com.drestation.anythingpics

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.drestation.anythingpics.databinding.ActivityCreatePinBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CreatePin : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePinBinding // Enable binding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()

        binding.createBtn.setOnClickListener {
            uploadImage()
        }
    }

    /*private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        // success?
    }*/

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun uploadImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["image"] = uri.toString()

                        firebaseFirestore.collection("images").add(map)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        firestoreTask.exception?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}