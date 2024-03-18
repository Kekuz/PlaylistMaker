package com.example.playlistmaker.ui.playlist.model

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

sealed interface PlaylistViewState {
    data class PlaylistContent(
        val playlists: Playlist,
        val tracks: List<Track>,
    ) : PlaylistViewState

    data class BottomViewContent(
        val tracks: List<Track>
    ) : PlaylistViewState


}