package com.example.playlistmaker.data.settings.storage

import android.content.Context
import com.example.playlistmaker.data.settings.SettingsStorage
import com.example.playlistmaker.data.settings.storage.dto.ThemeSettingsDto

class SharedPrefSettingsStorage(context: Context) : SettingsStorage {

    private val sharedPreferences = context.getSharedPreferences(NIGHT_MODE, Context.MODE_PRIVATE)
    override fun save(isNight: ThemeSettingsDto) {
        sharedPreferences.edit().putBoolean(IS_NIGHT, isNight.isNightMode).apply()
    }

    override fun get(): ThemeSettingsDto {
        val isNight = sharedPreferences.getBoolean(IS_NIGHT, DEFAULT_NIGHT_MODE)
        return ThemeSettingsDto(isNight)
    }

    private companion object {
        const val NIGHT_MODE = "night_mode"
        const val IS_NIGHT = "is_night"
        const val DEFAULT_NIGHT_MODE = false
    }

}