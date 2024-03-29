package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.SearchHistoryStorage
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track

class SearchHistoryRepositoryImpl(private val searchHistoryStorage: SearchHistoryStorage) :
    SearchHistoryRepository {

    override suspend fun getTracksFromStorage(): List<Track> {
        return searchHistoryStorage.getTracks()
    }

    override fun putTracksToStorage(tracks: List<Track>) {
        searchHistoryStorage.putTracks(tracks)
    }

}