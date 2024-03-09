package com.example.playlistmaker.data.favorites.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favorites.database.dao.TrackDao
import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity

@Database(
    entities = [TrackDatabaseEntity::class],
    version = 1
)
abstract class TrackDatabase : RoomDatabase() {
    abstract val trackDao: TrackDao?
}