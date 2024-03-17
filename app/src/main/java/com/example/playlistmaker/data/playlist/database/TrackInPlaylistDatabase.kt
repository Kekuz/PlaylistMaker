package com.example.playlistmaker.data.playlist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.playlist.database.dao.TrackInPlaylistDao
import com.example.playlistmaker.data.playlist.database.model.TrackInPlaylistDatabaseEntity

@Database(
    entities = [TrackInPlaylistDatabaseEntity::class],
    version = 1
)
abstract class TrackInPlaylistDatabase : RoomDatabase() {
    abstract val trackInPlaylistDao: TrackInPlaylistDao?
}