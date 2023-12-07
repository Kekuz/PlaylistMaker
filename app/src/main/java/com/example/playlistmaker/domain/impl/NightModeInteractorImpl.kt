package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.interactor.NightModeInteractor
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.models.NightMode

class NightModeInteractorImpl(private val repository: NightModeRepository): NightModeInteractor {
    override fun saveNightMode(nightMode: NightMode) {
        repository.saveNightMode(nightMode)
    }

    override fun getNightMode(): NightMode {
        return repository.getNightMode()
    }
}