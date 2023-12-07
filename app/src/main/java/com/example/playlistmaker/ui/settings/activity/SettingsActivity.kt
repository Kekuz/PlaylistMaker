package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                Creator.provideSharingInteractor(),
                Creator.provideSettingsInteractor(),
            )
        )[SettingsViewModel::class.java]

        binding.themeSwitcher.isChecked = viewModel.getSettingsInteractor().getSettings().isNightMode

        bindingClickListeners()
        bindingThemeSwitcher()

    }

    private fun bindingClickListeners() = with(binding) {
        ivBackArrowBtn.setOnClickListener {
            finish()
        }

        shareAppFl.setOnClickListener {
            viewModel.getSharingInteractor().shareApp()
        }

        writeToSupportFl.setOnClickListener {
            viewModel.getSharingInteractor().openSupport()
        }

        userAgreementFl.setOnClickListener {
            viewModel.getSharingInteractor().openTerms()
        }
    }

    private fun bindingThemeSwitcher() = with(binding) {
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.getSettingsInteractor().saveSettings(ThemeSettings(checked))
            viewModel.getSettingsInteractor().changeTheme()
        }
    }
}