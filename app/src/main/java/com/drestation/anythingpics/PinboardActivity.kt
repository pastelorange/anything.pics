package com.drestation.anythingpics

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.drestation.anythingpics.databinding.ActivityPinboardBinding

// This activity renders activity_pinboard
class PinboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PinViewModel by viewModels()
        viewModel.getPins().observe(this) {
            binding.recyclerView.adapter = PinAdapter(it)
            binding.recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.goToCreatePinActivityButton.setOnClickListener {
            startActivity(Intent(this, CreatePinActivity::class.java))
        }
        // https://www.geeksforgeeks.org/android-recyclerview-as-staggered-grid-with-kotlin/
    }
}