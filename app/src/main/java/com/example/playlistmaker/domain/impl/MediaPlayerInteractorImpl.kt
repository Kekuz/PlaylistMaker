package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaPlayer
import com.example.playlistmaker.domain.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.PlayerStates

class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayer): MediaPlayerInteractor {
    override fun prepareMediaPlayer(consumer: () -> Unit) {
        mediaPlayer.prepareMediaPlayer(consumer)
    }

    override fun pausePlayer() {
        mediaPlayer.pausePlayer()
    }

    override fun playbackControl(consumer: (PlayerStates) -> Unit) {
        mediaPlayer.playbackControl(consumer)
    }

    override fun releasePlayer() {
        mediaPlayer.releasePlayer()
    }

    override fun isPlayerStatePlaying(): Boolean {
        return mediaPlayer.isPlayerStatePlaying()
    }

    override fun getCurrentPosition(): String {
        return mediaPlayer.getCurrentPosition()
    }
}