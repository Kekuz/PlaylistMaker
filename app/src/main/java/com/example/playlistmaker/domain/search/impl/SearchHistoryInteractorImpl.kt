package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override suspend fun getTrackHistory(): List<Track> {
        return CoroutineScope(Dispatchers.IO).async {
            repository.getTracksFromStorage().toMutableList()
        }.await()
    }

    override suspend fun addToTrackHistory(track: Track) {
        val temp = getTrackHistory().toMutableList()
        temp.addToTrackArray(track)
        repository.putTracksToStorage(temp)
    }

    override fun clearTrackHistory() {
        repository.putTracksToStorage(listOf())
    }

    private fun MutableList<Track>.addToTrackArray(track: Track) {
        var alreadyInside = false

        for (x in this) {
            if (x.trackId == track.trackId) alreadyInside = true
        }

        if (alreadyInside) remove(track)

        if (size >= TRACKS_IN_HISTORY) removeAt(0)

        add(track)
    }

    private companion object {
        const val TRACKS_IN_HISTORY = 10
    }
}