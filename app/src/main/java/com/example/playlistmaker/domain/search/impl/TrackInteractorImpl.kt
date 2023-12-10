package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(
        term: String,
        consumer: (foundTracksResource: List<Track>?, errorMessage: String?) -> Unit
    ) {
        executor.execute {
            when (val resource = repository.search(term)) {
                is Resource.Success -> {
                    consumer.invoke(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.invoke(null, resource.message)
                }
            }
        }
    }
}