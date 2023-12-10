package com.example.playlistmaker.domain.search.api.interactor

import com.example.playlistmaker.domain.search.models.Track

interface TrackInteractor {
    fun searchTrack(
        term: String,
        consumer: (foundTracksResource: List<Track>?, errorMessage: String?) -> Unit
    )
}