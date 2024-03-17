package com.example.playlistmaker.data.playlist.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlist_database")
data class PlaylistDatabaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?,
    val pathToCover: String?,
    val trackIdsList: List<String> = listOf(),
    val tracksCount: Int = 0,
)