package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    private val tracks = repository.getTracksFromStorage().toMutableList()

    override fun getTrackHistory(): List<Track> {
        return tracks
    }

    override fun addToTrackHistory(track: Track) {
        tracks.addToTrackArray(track)
        repository.putTracksToStorage(tracks)
    }

    override fun clearTrackHistory() {
        tracks.clear()
        repository.putTracksToStorage(listOf())
    }

    private fun MutableList<Track>.addToTrackArray(track: Track) {
        var alreadyInside = false

        for (x in this) {
            if (x == track) alreadyInside = true
        }

        if (alreadyInside) remove(track)

        if (size >= TRACKS_IN_HISTORY) removeAt(0)

        add(track)
    }

    private companion object {
        const val TRACKS_IN_HISTORY = 10
    }
}