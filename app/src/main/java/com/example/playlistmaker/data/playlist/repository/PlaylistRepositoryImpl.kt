package com.example.playlistmaker.data.playlist.repository

import com.example.playlistmaker.data.mapper.DatabaseMapper
import com.example.playlistmaker.data.playlist.database.PlaylistDatabase
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.domain.playlist.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(private val database: PlaylistDatabase) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist): Unit = withContext(Dispatchers.IO) {
        database.playlistDao?.insert(DatabaseMapper.map(playlist))
    }
}