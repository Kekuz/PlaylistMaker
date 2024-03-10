package com.example.playlistmaker.domain.playlist.model

data class Playlist(
    //val id: Int,
    val name: String,
    val description: String?,
    val pathToCover: String?,
    val trackIdsList: List<String> = listOf(),
    val tracksCount: Int = 0,
)