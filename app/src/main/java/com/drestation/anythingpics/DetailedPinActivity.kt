package com.drestation.anythingpics

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drestation.anythingpics.databinding.ActivityDetailedPinBinding
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
    }
}