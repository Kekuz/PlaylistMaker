package com.example.playlistmaker.ui.audioplayer.models

import com.example.playlistmaker.domain.search.models.Track


sealed interface AudioPlayerViewState {
    data class Content(val track: Track, val player: PlayerView) : AudioPlayerViewState
    data class Player(val player: PlayerView) : AudioPlayerViewState
}