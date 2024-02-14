package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.network.dto.TrackResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): TrackResponse
}