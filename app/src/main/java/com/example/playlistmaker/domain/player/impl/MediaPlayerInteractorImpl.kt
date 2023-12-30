package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.api.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerStates

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository,
) : MediaPlayerInteractor {
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

    override fun trackEndingCheck(consumer: () -> Unit) {
        return mediaPlayerRepository.trackEndingCheck(consumer)
    }

    override fun getTimerRefreshDelayMillis(): Long =
        mediaPlayerRepository.getTimerRefreshDelayMillis()
}