package com.example.playlistmaker.domain.api.interactor

import com.example.playlistmaker.domain.models.NightMode

interface NightModeInteractor {

    fun saveNightMode(nightMode: NightMode)

    fun getNightMode(): NightMode
}