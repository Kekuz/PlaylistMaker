package com.example.playlistmaker.domain.search.api.interactor

import com.example.playlistmaker.domain.search.models.Track

interface TrackInteractor {
    fun searchTrack(term: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracksResource: List<Track>?, errorMessage: String?)
    }
}