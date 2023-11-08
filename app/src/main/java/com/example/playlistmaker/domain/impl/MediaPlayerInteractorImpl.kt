package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.PlayerStates

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository): MediaPlayerInteractor {
    override fun prepareMediaPlayer(consumer: () -> Unit) {
        mediaPlayerRepository.prepareMediaPlayer(consumer)
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun playbackControl(consumer: (PlayerStates) -> Unit) {
        mediaPlayerRepository.playbackControl(consumer)
    }

    override fun releasePlayer() {
        mediaPlayerRepository.releasePlayer()
    }

    override fun isPlayerStatePlaying(): Boolean {
        return mediaPlayerRepository.isPlayerStatePlaying()
    }

    override fun getCurrentPosition(): String {
        return mediaPlayerRepository.getCurrentPosition()
    }
}