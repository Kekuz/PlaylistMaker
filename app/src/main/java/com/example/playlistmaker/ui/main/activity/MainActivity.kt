package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.activity.MediaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val mediaBtn = findViewById<Button>(R.id.media_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)

        val searchBtnClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        searchBtn.setOnClickListener(searchBtnClickListener)

        mediaBtn.setOnClickListener{
            val intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}