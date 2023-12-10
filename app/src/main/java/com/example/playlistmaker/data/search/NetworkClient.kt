package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.network.dto.TrackResponse

interface NetworkClient {
    fun doRequest(dto: Any): TrackResponse
}