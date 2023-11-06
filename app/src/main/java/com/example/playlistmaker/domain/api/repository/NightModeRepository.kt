package com.example.playlistmaker.domain.api.repository

import com.example.playlistmaker.domain.models.NightMode

interface NightModeRepository {

    fun saveNightMode(nightMode: NightMode)

    fun getNightMode(): NightMode
}