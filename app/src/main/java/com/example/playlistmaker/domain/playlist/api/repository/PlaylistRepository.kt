package com.example.playlistmaker.domain.playlist.api.repository

import com.example.playlistmaker.domain.playlist.model.Playlist

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)
}