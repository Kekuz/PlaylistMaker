package com.example.playlistmaker.data.settings

import com.example.playlistmaker.data.settings.storage.dto.ThemeSettingsDto

interface SettingsStorage {

    fun save(themeSettings: ThemeSettingsDto)

    fun get(): ThemeSettingsDto

}