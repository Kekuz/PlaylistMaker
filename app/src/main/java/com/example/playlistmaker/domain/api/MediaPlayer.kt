package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerStates

interface MediaPlayer {

    fun prepareMediaPlayer(consumer: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(consumer: (PlayerStates) -> Unit)

    fun releasePlayer()

    fun isPlayerStatePlaying():Boolean

    fun getCurrentPosition(): String
}