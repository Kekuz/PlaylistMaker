package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.search.models.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(term: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.search(term)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}