package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.api.interactor.SettingsInteractor
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.domain.sharing.api.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {


    fun saveNightMode(isNight: Boolean) = settingsInteractor.saveSettings(ThemeSettings(isNight))
    fun changeTheme() = settingsInteractor.changeTheme()
    fun getNightMode() = settingsInteractor.getSettings().isNightMode
    fun shareApp() = sharingInteractor.shareApp()
    fun openSupport() = sharingInteractor.openSupport()
    fun openTerms() = sharingInteractor.openTerms()
}