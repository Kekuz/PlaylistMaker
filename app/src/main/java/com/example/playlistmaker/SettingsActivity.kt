package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backArrowBtn = findViewById<ImageView>(R.id.iv_back_arrow_btn)
        val shareAppBtn = findViewById<FrameLayout>(R.id.share_app_fl)
        val writeToSupportBtn = findViewById<FrameLayout>(R.id.write_to_support_fl)
        val userAgreementBtn = findViewById<FrameLayout>(R.id.user_agreement_fl)

        backArrowBtn.setOnClickListener {
            finish()
        }

        shareAppBtn.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                val url = getString(R.string.share_url)
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url);
                startActivity(this)
            }
        }

        writeToSupportBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                val subject = getString(R.string.support_subject)
                val message = getString(R.string.support_message)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, subject);
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }
        }

        userAgreementBtn.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.practicum_offer))
                startActivity(this)
            }
        }
    }
}