package com.example.playlistmaker.domain.favorites.api.interactor

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun saveFavorite(track: Track)

    suspend fun deleteFavorite(track: Track)

    fun getFavorites(): Flow<List<Track>>
}