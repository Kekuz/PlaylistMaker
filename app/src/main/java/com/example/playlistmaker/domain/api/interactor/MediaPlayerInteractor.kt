package com.example.playlistmaker.domain.api.interactor

import com.example.playlistmaker.domain.models.PlayerStates


interface MediaPlayerInteractor {

    fun prepareMediaPlayer(consumer: () -> Unit)

    fun pausePlayer()

    fun playbackControl(consumer: (PlayerStates) -> Unit)

    fun releasePlayer()

    fun isPlayerStatePlaying():Boolean

    fun getCurrentPosition(): String

}