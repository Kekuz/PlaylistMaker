package com.example.playlistmaker.data.search.network.dto

data class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>,
) : TrackResponse()