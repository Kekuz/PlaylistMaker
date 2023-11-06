package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.App.Companion.darkTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repository.NightModeRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefNightModeStorage
import com.example.playlistmaker.domain.api.interactor.NightModeInteractor
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.impl.NightModeInteractorImpl
import com.example.playlistmaker.domain.models.NightMode
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    //Как черт создаю дважды, потому что тут надо передавать контекст, а без DI я не знаю как это делать
    //Просто статик класс с контекстом не катит
    private fun getNightModeRepository(): NightModeRepository {
        return NightModeRepositoryImpl(SharedPrefNightModeStorage(applicationContext))
    }

    private fun provideNightModeInteractor(): NightModeInteractor {
        return NightModeInteractorImpl(getNightModeRepository())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backArrowBtn = findViewById<ImageView>(R.id.iv_back_arrow_btn)
        val shareAppBtn = findViewById<FrameLayout>(R.id.share_app_fl)
        val writeToSupportBtn = findViewById<FrameLayout>(R.id.write_to_support_fl)
        val userAgreementBtn = findViewById<FrameLayout>(R.id.user_agreement_fl)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        ///val sharedPrefs = getSharedPreferences(NIGHT_MODE, MODE_PRIVATE)

        themeSwitcher.isChecked = darkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            provideNightModeInteractor().saveNightMode(NightMode(checked))
            /*sharedPrefs.edit()
                .putString(NIGHT_MODE, checked.toString())
                .apply()*/
        }

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