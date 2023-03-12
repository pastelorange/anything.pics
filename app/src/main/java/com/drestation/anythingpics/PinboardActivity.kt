package com.drestation.anythingpics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drestation.anythingpics.databinding.ActivityPinboardBinding
import androidx.activity.viewModels

class PinboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PinViewModel by viewModels()
        viewModel.getPins().observe(this) {
            binding.recyclerView.adapter = PinAdapter(it)
        }
    }
}