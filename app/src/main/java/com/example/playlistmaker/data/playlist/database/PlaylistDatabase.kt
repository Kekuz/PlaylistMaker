package com.example.playlistmaker.data.playlist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.data.playlist.database.conventer.Converter
import com.example.playlistmaker.data.playlist.database.dao.PlaylistDao
import com.example.playlistmaker.data.playlist.database.model.PlaylistDatabaseEntity

@Database(
    entities = [PlaylistDatabaseEntity::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract val playlistDao: PlaylistDao?
}