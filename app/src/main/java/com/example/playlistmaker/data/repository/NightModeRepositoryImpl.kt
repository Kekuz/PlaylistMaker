package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NightModeStorage
import com.example.playlistmaker.data.storage.dto.NightModeDto
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.models.NightMode

class NightModeRepositoryImpl(private val nightModeStorage: NightModeStorage): NightModeRepository {

    //Принимаем domain модель и кладем в data модель
    override fun saveNightMode(nightMode: NightMode) {
        val nightModeDto = NightModeDto(nightMode.isNight)
        nightModeStorage.save(nightModeDto)
    }

    //Берем data модель и отдаем domain модель
    override fun getNightMode(): NightMode {
        val nightModeDto = nightModeStorage.get()
        return NightMode(nightModeDto.isNight)
    }
}