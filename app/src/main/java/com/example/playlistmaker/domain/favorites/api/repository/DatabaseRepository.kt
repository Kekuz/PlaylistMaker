package com.example.playlistmaker.domain.favorites.api.repository

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun save(track: Track)

    suspend fun delete(track: Track)

    fun getTracks(): Flow<List<Track>>

}

