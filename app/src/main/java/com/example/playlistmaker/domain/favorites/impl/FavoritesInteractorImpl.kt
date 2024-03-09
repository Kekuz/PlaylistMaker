package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.api.interactor.FavoritesInteractor
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesInteractorImpl(private val trackRepository: TrackRepository) :
    FavoritesInteractor {
    override suspend fun saveFavorite(track: Track) {
        trackRepository.save(track)
    }

    override suspend fun deleteFavorite(track: Track) {
        trackRepository.delete(track)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return trackRepository.getTracks().map { list ->
            list.sortedByDescending { track -> track.timeCreated }
        }
    }

}