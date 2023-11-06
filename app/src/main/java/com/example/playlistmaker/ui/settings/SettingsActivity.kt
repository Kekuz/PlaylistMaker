package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.App.Companion.darkTheme
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.NightMode

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.isChecked = darkTheme

        bindingClickListeners()
        bindingThemeSwitcher()

    }

    private fun bindingClickListeners() = with(binding) {
        ivBackArrowBtn.setOnClickListener {
            finish()
        }

        shareAppFl.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                val url = getString(R.string.share_url)
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url);
                startActivity(this)
            }
        }

        writeToSupportFl.setOnClickListener {
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

        userAgreementFl.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.practicum_offer))
                startActivity(this)
            }
        }
    }

    private fun bindingThemeSwitcher() = with(binding) {
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            Creator.provideNightModeInteractor().saveNightMode(NightMode(checked))
        }
    }
}