package com.example.playlistmaker.domain.search.api.interactor

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryInteractor {

    suspend fun getTrackHistory():List<Track>

    suspend fun addToTrackHistory(track: Track)

    fun clearTrackHistory()
}