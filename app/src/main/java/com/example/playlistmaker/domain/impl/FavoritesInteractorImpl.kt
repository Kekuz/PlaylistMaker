package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.favorites.api.interactor.FavoritesInteractor
import com.example.playlistmaker.domain.favorites.api.repository.DatabaseRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesInteractorImpl(private val databaseRepository: DatabaseRepository) :
    FavoritesInteractor {
    override suspend fun saveFavorite(track: Track) {
        databaseRepository.save(track)
    }

    override suspend fun deleteFavorite(track: Track) {
        databaseRepository.delete(track)
    }

    //TODO Сортировочка по id надо сделать по-другому
    override fun getFavorites(): Flow<List<Track>> {
        return databaseRepository.getTracks().map {list ->
            list.sortedBy { track -> track.trackId }
        }
    }

}