package com.example.playlistmaker.ui.edit_playlist.model

import com.example.playlistmaker.domain.model.Playlist

sealed interface EditPlaylistViewState {
    data class Content(val playlist: Playlist) : EditPlaylistViewState
}