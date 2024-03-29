package com.example.playlistmaker.data.favorites.database

import com.example.playlistmaker.data.favorites.DatabaseClient
import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity

class RoomDatabaseClient(private val database: TrackDatabase) : DatabaseClient {

    override fun save(track: TrackDatabaseEntity) {
        database.trackDao?.insert(track)
    }

    override fun delete(track: TrackDatabaseEntity) {
        database.trackDao?.delete(track)
    }

    override fun getTracks(): List<TrackDatabaseEntity> {
        return database.trackDao?.get() ?: emptyList()
    }

    override fun getIds(): List<Int> {
        return database.trackDao?.getIds() ?: emptyList()
    }
}