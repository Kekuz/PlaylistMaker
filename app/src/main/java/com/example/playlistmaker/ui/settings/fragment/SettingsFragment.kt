package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.themeSwitcher.isChecked = viewModel.getSettingsInteractor().getSettings().isNightMode

        bindingClickListeners()
        bindingThemeSwitcher()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindingClickListeners() = with(binding) {
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