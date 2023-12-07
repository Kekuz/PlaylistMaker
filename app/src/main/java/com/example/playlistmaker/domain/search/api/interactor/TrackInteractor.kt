package com.example.playlistmaker.domain.search.api.interactor

import com.example.playlistmaker.domain.search.models.Resource

interface TrackInteractor {
    fun searchTrack(term: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracksResource: Resource)
    }
}