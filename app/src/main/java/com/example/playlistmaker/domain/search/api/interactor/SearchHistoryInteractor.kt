package com.example.playlistmaker.domain.search.api.interactor

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryInteractor {

    fun getTrackHistory():List<Track>

    fun addToTrackHistory(track: Track)

    fun clearTrackHistory()
}