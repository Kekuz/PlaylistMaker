package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.themeSwitcher.isChecked = viewModel.getNightMode()

        bindingClickListeners()
        bindingThemeSwitcher()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindingClickListeners() = with(binding) {
        shareAppFl.setOnClickListener {
            viewModel.shareApp()
        }

        writeToSupportFl.setOnClickListener {
            viewModel.openSupport()
        }

        userAgreementFl.setOnClickListener {
            viewModel.openTerms()
        }
    }

    private fun bindingThemeSwitcher() = with(binding) {
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveNightMode(checked)
            viewModel.changeTheme()
        }
    }
}