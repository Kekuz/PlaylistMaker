package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backArrowBtn = findViewById<ImageView>(R.id.iv_back_arrow_btn)
        val shareAppBtn = findViewById<FrameLayout>(R.id.share_app_fl)
        val writeToSupportBtn = findViewById<FrameLayout>(R.id.write_to_support_fl)
        val userAgreementBtn = findViewById<FrameLayout>(R.id.user_agreement_fl)

        backArrowBtn.setOnClickListener{
            finish()
        }

        shareAppBtn.setOnClickListener{
            val url = getString(R.string.share_url)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(intent)
        }

        writeToSupportBtn.setOnClickListener {
            val subject = getString(R.string.support_subject)
            val message = getString(R.string.support_message)

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }

        userAgreementBtn.setOnClickListener {
            val url = Uri.parse(getString(R.string.practicum_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}