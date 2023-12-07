package com.example.playlistmaker.domain.player.api.interactor

import com.example.playlistmaker.domain.player.models.PlayerStates


interface MediaPlayerInteractor {

    fun prepareMediaPlayer(consumer: () -> Unit)

    fun pausePlayer()

    fun playbackControl(consumer: (PlayerStates) -> Unit)

    fun releasePlayer()

    fun isPlayerStatePlaying():Boolean

    fun getCurrentPosition(): String

}