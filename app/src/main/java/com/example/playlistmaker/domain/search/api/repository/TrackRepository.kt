package com.example.playlistmaker.domain.search.api.repository

import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun search(term: String): Flow<Resource<List<Track>>>
}