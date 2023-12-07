package com.example.playlistmaker.domain.settings.api.interactor

import com.example.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsInteractor {

    fun saveSettings(themeSettings: ThemeSettings)

    fun getSettings(): ThemeSettings

    fun changeTheme()
}