package com.example.playlistmaker.data.playlist.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("track_in_playlist_database")
data class TrackInPlaylistDatabaseEntity(
    @PrimaryKey
    val id: Int,
    val timeCreated: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
)