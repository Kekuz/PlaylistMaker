package com.example.playlistmaker.domain.search.models

data class Resource(
    val track: List<Track>?,
    val responseCode: Int,
)