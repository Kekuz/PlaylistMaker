package com.example.playlistmaker.domain.playlist.api.interactor

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

interface PlaylistInteractor {
    suspend fun savePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): List<Playlist>

    suspend fun getPlaylistById(id: Int): Playlist?

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun getTracksFromPlaylistByIds(ids: List<String>): List<Track>
}