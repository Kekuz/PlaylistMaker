package com.example.playlistmaker.data.playlist.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.mapper.DatabaseMapper
import com.example.playlistmaker.data.playlist.database.PlaylistDatabase
import com.example.playlistmaker.data.playlist.database.TrackInPlaylistDatabase
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val trackInPlaylistDatabase: TrackInPlaylistDatabase,
    private val context: Context
) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist): Unit = withContext(Dispatchers.IO) {
        playlistDatabase.playlistDao?.insert(DatabaseMapper.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist): Unit = withContext(Dispatchers.IO) {
        playlistDatabase.playlistDao?.insert(DatabaseMapper.mapPlaylistWithId(playlist))
    }

    override suspend fun getPlaylists(): List<Playlist> = withContext(Dispatchers.IO) {
        return@withContext playlistDatabase.playlistDao?.getAll()?.map { DatabaseMapper.map(it) }
            ?: emptyList()
    }

    override suspend fun getPlaylistById(id: Int): Playlist? = withContext(Dispatchers.IO) {
        return@withContext DatabaseMapper.mapNullable(playlistDatabase.playlistDao?.getById(id))
    }

    override fun getImageFromPrivateStorage(fileName: String?): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
        val file = fileName?.let { File(filePath, it) }
        return file?.toUri().toString()
    }

    override fun saveImageToPrivateStorage(uri: String, fileName: String) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$fileName.jpg")
        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Unit =
        withContext(Dispatchers.IO) {
            trackInPlaylistDatabase.trackInPlaylistDao?.insert(DatabaseMapper.mapToPlaylist(track))
            playlistDatabase.playlistDao?.insert(
                DatabaseMapper.mapAndAddTrackToPlaylist(
                    playlist,
                    track
                )
            )
        }

    override suspend fun getTracksFromPlaylistByIds(ids: List<String>): List<Track> {
        val tracks = mutableListOf<Track>()
        withContext(Dispatchers.IO) {
            val allTracks = trackInPlaylistDatabase.trackInPlaylistDao?.getAll()
                ?.map { DatabaseMapper.mapFromPlaylist(it) }
            ids.forEach { id ->
                allTracks?.find { it.trackId.toString() == id }?.let { tracks.add(it) }
            }
        }
        return tracks
    }

    override suspend fun deleteTrackFromPlaylist(id: String, playlist: Playlist): Unit =
        withContext(Dispatchers.IO) {
            playlistDatabase.playlistDao?.insert(
                DatabaseMapper.mapAndDeleteTrackFromPlaylist(
                    id,
                    playlist,
                )
            )

            deleteFromTotalPlaylist(id)
        }

    override suspend fun deletePlaylist(playlist: Playlist) =
        withContext(Dispatchers.IO) {
            playlistDatabase.playlistDao?.delete(
                DatabaseMapper.mapPlaylistWithId(
                    playlist,
                )
            )

            playlist.trackIdsList.forEach {
                deleteFromTotalPlaylist(it)
            }
        }


    private suspend fun deleteFromTotalPlaylist(id: String) {
        val totalPlaylists = getPlaylists()
        var matchesNumber = 0
        totalPlaylists.forEach {
            if (it.trackIdsList.contains(id)) matchesNumber++
        }
        if (matchesNumber == 0) {
            val deletedTrack = getTracksFromPlaylistByIds(listOf(id))[0]
            trackInPlaylistDatabase.trackInPlaylistDao?.delete(
                DatabaseMapper.mapToPlaylist(
                    deletedTrack
                )
            )
        }
    }


    companion object {
        const val DIRECTORY = "playlist"
    }
}