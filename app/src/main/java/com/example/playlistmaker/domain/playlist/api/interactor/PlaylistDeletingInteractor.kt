package com.example.playlistmaker.domain.playlist.api.interactor

import com.example.playlistmaker.domain.model.Playlist

interface PlaylistDeletingInteractor {
    suspend fun deleteTrackFromPlaylist(id: String, playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)
}