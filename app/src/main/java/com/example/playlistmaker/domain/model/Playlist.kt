package com.example.playlistmaker.domain.model

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val pathToCover: String?,
    val trackIdsList: List<String> = listOf(),
    val tracksCount: Int = 0,
)