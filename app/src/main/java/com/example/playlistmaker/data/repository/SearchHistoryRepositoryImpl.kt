package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.network.SearchHistoryStorage
import com.example.playlistmaker.domain.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl(private val searchHistoryStorage: SearchHistoryStorage) : SearchHistoryRepository {

    override fun getTracksFromStorage(): MutableList<Track> {
        return searchHistoryStorage.getTracks()
    }

    override fun putTracksToStorage(tracks: List<Track>) {
        searchHistoryStorage.putTracks(tracks)
    }

}