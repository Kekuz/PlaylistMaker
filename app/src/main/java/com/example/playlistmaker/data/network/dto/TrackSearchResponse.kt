package com.example.playlistmaker.data.network.dto

data class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>,
) : Response()