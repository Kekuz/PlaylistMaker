package com.example.playlistmaker.search

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>,
)
