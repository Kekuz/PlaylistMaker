package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backArrowBtn = findViewById<ImageView>(R.id.iv_back_arrow_btn)

        backArrowBtn.setOnClickListener{
            finish()
        }
    }
}