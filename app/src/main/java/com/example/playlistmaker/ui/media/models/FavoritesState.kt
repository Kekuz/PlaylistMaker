package com.example.playlistmaker.ui.media.models

import com.example.playlistmaker.domain.model.Track


sealed interface FavoritesState {
    object Empty : FavoritesState
    data class Content(
        val tracks: List<Track>
    ) : FavoritesState
}