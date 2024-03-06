package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity

interface DatabaseClient {
    fun save(track: TrackDatabaseEntity)

    fun delete(track: TrackDatabaseEntity)

    fun getTracks(): List<TrackDatabaseEntity>

    fun getIds(): List<Int>



}