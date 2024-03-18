package com.example.playlistmaker.domain.playlist.api.repository

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): List<Playlist>

    suspend fun getPlaylistById(id: Int): Playlist?

    fun getImageFromPrivateStorage(fileName: String): String

    fun saveImageToPrivateStorage(uri: String, fileName: String)

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun getTracksFromPlaylistByIds(ids: List<String>): List<Track>

    suspend fun deleteTrackFromPlaylist(id: String, playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)
}