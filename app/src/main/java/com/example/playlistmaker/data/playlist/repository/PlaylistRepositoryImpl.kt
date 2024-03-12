package com.example.playlistmaker.data.playlist.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.mapper.DatabaseMapper
import com.example.playlistmaker.data.playlist.database.PlaylistDatabase
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(private val database: PlaylistDatabase) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist): Unit = withContext(Dispatchers.IO) {
        database.playlistDao?.insert(DatabaseMapper.map(playlist))
    }

    override suspend fun getPlaylists(): List<Playlist> = withContext(Dispatchers.IO) {
        return@withContext database.playlistDao?.getAll()?.map { DatabaseMapper.map(it) }
            ?: emptyList()
    }
}