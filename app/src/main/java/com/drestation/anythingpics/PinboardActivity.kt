package com.drestation.anythingpics

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun itemSelected(pin: Pin) {
        Log.i("Pin selected", "$pin")
        val intent = Intent(this, DetailedPinActivity::class.java)
        intent.putExtra("pin", pin)
        startActivity(intent)
    }
}