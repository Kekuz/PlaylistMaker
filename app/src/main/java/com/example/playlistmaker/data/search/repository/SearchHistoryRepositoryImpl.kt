package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.SearchHistoryStorage
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchHistoryRepositoryImpl(private val searchHistoryStorage: SearchHistoryStorage) :
    SearchHistoryRepository {

    override fun getTracksFromStorage(): MutableList<Track> {
        return searchHistoryStorage.getTracks()
    }

    override fun putTracksToStorage(tracks: List<Track>) {
        searchHistoryStorage.putTracks(tracks)
    }

}