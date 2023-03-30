package com.drestation.anythingpics

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drestation.anythingpics.databinding.ActivityDetailedPinBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class DetailedPinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Pin object from previous activity
        val pin = intent.getParcelableExtra<Pin>("pin")

        // Populate elements
        binding.titleTextView.text = pin!!.title
        binding.captionTextView.text = pin.caption
        Picasso.get().load(pin.imageUrl).into(binding.imageView)

        binding.deleteButton.setOnClickListener {
            // Delete pin from Firestore
            val db = FirebaseFirestore.getInstance().collection(pin.uid!!)
            db.document(pin.documentId!!).delete()

            // Delete image file from Storage
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pin.imageUrl!!)
            storageRef.delete()

            Toast.makeText(this, "Pin deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}