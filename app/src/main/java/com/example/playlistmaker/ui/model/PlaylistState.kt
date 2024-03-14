package com.example.playlistmaker.ui.model

import com.example.playlistmaker.domain.model.Playlist

sealed interface PlaylistState {
    object Empty : PlaylistState
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistState


}