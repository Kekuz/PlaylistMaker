package com.example.playlistmaker.data

import com.example.playlistmaker.data.storage.dto.NightModeDto

interface NightModeStorage {

    fun save(isNight: NightModeDto)

    fun get(): NightModeDto

}