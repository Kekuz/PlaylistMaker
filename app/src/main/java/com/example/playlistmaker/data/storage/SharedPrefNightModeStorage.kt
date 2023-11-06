package com.example.playlistmaker.data.storage

import android.content.Context
import com.example.playlistmaker.data.NightModeStorage
import com.example.playlistmaker.data.storage.dto.NightModeDto

class SharedPrefNightModeStorage(context: Context) :NightModeStorage {

    private val sharedPreferences = context.getSharedPreferences(NIGHT_MODE, Context.MODE_PRIVATE)
    override fun save(isNight: NightModeDto) {
        sharedPreferences.edit().putBoolean(IS_NIGHT, isNight.isNight).apply()
    }

    override fun get(): NightModeDto {
        val isNight = sharedPreferences.getBoolean(IS_NIGHT, DEFAULT_NIGHT_MODE)
        return NightModeDto(isNight)
    }

    private companion object{
        const val NIGHT_MODE = "night_mode"
        const val IS_NIGHT = "is_night"
        const val DEFAULT_NIGHT_MODE = false
    }

}