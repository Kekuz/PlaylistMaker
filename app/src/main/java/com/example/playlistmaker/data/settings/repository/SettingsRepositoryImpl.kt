package com.example.playlistmaker.data.settings.repository

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.SettingsStorage
import com.example.playlistmaker.data.settings.storage.dto.ThemeSettingsDto
import com.example.playlistmaker.domain.settings.api.repository.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(private val settingsStorage: SettingsStorage) : SettingsRepository {

    //Принимаем domain модель и кладем в data модель
    override fun saveSettings(themeSettings: ThemeSettings) {
        val themeSettingsDto = ThemeSettingsDto(themeSettings.isNightMode)
        settingsStorage.save(themeSettingsDto)
    }

    //Берем data модель и отдаем domain модель
    override fun getSettings(): ThemeSettings {
        val settingsDto = settingsStorage.get()
        return ThemeSettings(settingsDto.isNightMode)
    }

    //Меняем тему приложения
    override fun changeTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (getSettings().isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }


}