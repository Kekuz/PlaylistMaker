package com.example.playlistmaker.ui.playlists.model

import com.example.playlistmaker.domain.model.Playlist

sealed interface PlaylistsState {
    object Empty : PlaylistsState
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState


}