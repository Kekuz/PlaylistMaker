package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(
        term: String,
    ) : Flow<Pair<List<Track>?, String?>> {
        return repository.search(term).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Pair(resource.data, null)
                }

                is Resource.Error -> {
                    Pair(null, resource.message)
                }
            }
        }

    }
}