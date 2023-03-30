package com.drestation.anythingpics

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.drestation.anythingpics.databinding.ActivityPinboardBinding

// This activity renders activity_pinboard
class PinboardActivity : AppCompatActivity(), PinAdapter.ItemListener {
    private lateinit var binding: ActivityPinboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PinViewModel by viewModels()
        viewModel.getPins().observe(this) {
            binding.recyclerView.adapter = PinAdapter(it, this)
        }

        binding.goToCreatePinActivityButton.setOnClickListener {
            startActivity(Intent(this, CreatePinActivity::class.java))
        }
    }

    // On item selection, move to details activity and pass the Pin object
    override fun itemSelected(pin: Pin) {
        val intent = Intent(this, DetailedPinActivity::class.java)
        intent.putExtra("pin", pin)
        startActivity(intent)
    }
}