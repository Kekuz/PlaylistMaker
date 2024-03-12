package com.example.playlistmaker.domain.playlist.api.repository

import com.example.playlistmaker.domain.model.Playlist

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): List<Playlist>

    fun getImageFromPrivateStorage(fileName: String): String

    fun saveImageToPrivateStorage(uri: String, fileName: String)
}