package com.example.playlistmaker.domain.search.api.repository

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    suspend fun getTracksFromStorage(): List<Track>

    fun putTracksToStorage(tracks: List<Track>)

}