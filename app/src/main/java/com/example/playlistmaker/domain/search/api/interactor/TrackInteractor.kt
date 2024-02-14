package com.example.playlistmaker.domain.search.api.interactor

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(
        term: String,
    ): Flow<Pair<List<Track>?, String?>>
}