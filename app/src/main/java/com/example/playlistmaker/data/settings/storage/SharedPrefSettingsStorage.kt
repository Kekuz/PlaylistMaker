package com.example.playlistmaker.data.settings.storage

import android.content.SharedPreferences
import com.example.playlistmaker.data.settings.SettingsStorage
import com.example.playlistmaker.data.settings.storage.dto.ThemeSettingsDto

class SharedPrefSettingsStorage(
    private val sharedPreferences: SharedPreferences
) : SettingsStorage {

    override fun save(themeSettings: ThemeSettingsDto) {
        sharedPreferences.edit().putBoolean(IS_NIGHT, themeSettings.isNightMode).apply()
    }

    override fun get(): ThemeSettingsDto {
        val isNight = sharedPreferences.getBoolean(IS_NIGHT, DEFAULT_NIGHT_MODE)
        return ThemeSettingsDto(isNight)
    }

    private companion object {
        const val IS_NIGHT = "is_night"
        const val DEFAULT_NIGHT_MODE = false
    }

}