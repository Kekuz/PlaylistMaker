package com.example.playlistmaker.domain.player.api.repository

import com.example.playlistmaker.domain.player.models.PlayerStates

interface MediaPlayerRepository {

    fun prepareMediaPlayer(consumer: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(consumer: (PlayerStates) -> Unit)

    fun releasePlayer()

    fun isPlayerStatePlaying():Boolean

    fun getCurrentPosition(): String
}