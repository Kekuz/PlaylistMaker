package com.example.playlistmaker.domain.search.api.repository

import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun search(term: String): Flow<Resource<List<Track>>>

    suspend fun save(track: Track)

    suspend fun delete(track: Track)

    fun getTracks(): Flow<List<Track>>
}