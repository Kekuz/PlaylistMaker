package com.example.playlistmaker.domain.settings.api.repository

import com.example.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsRepository {

    fun saveSettings(themeSettings: ThemeSettings)

    fun getSettings(): ThemeSettings
    
}