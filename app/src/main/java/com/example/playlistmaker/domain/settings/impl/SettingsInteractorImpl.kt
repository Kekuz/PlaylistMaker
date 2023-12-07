package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.interactor.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.repository.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun saveSettings(themeSettings: ThemeSettings) {
        repository.saveSettings(themeSettings)
    }

    override fun getSettings(): ThemeSettings {
        return repository.getSettings()
    }

    override fun changeTheme() {
        repository.changeTheme()
    }


}