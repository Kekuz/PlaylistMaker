package com.example.playlistmaker.domain.search.api.repository

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryRepository {
    fun getTracksFromStorage(): List<Track>

    fun putTracksToStorage(tracks: List<Track>)

}