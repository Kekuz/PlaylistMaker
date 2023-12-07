package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.settings.api.interactor.SettingsInteractor
import com.example.playlistmaker.domain.sharing.api.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    fun getSharingInteractor(): SharingInteractor =
        sharingInteractor

    fun getSettingsInteractor(): SettingsInteractor =
        settingsInteractor
    companion object {
        fun getViewModelFactory(
            sharingInteractor: SharingInteractor,
            settingsInteractor: SettingsInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(sharingInteractor, settingsInteractor)
            }
        }
    }
}