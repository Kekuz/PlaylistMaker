package com.example.playlistmaker.ui.audioplayer.models

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

sealed interface AudioPlayerBottomSheetState {
    object EmptyPlaylists : AudioPlayerBottomSheetState
    data class ContentPlaylists(
        val playlists: List<Playlist>
    ) : AudioPlayerBottomSheetState

    data class TrackAdded(
        val track: Track
    ) : AudioPlayerBottomSheetState

    data class TrackAlreadyExist(
        val track: Track
    ) : AudioPlayerBottomSheetState
}