package com.drestation.anythingpics

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drestation.anythingpics.databinding.ActivityDetailedPinBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class DetailedPinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Supporting all API versions (else is deprecated)
        val pin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("pin", Pin::class.java)
        } else {
            intent.getParcelableExtra<Pin>("pin")
        }

        binding.titleTextView.text = pin!!.title
        binding.captionTextView.text = pin.title
        Picasso.get().load(pin.imageUrl).into(binding.imageView)

        binding.deletePinButton.setOnClickListener {
            // Query the database for pins made by the current user
            val db = FirebaseFirestore.getInstance().collection(pin.uid!!)
            db.document(pin.documentId!!).delete().addOnSuccessListener {
                Toast.makeText(this, "Pin deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}