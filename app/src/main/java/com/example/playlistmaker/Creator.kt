package com.example.playlistmaker

import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.api.repository.TrackRepository
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    private fun getMoviesRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): TrackInteractor {
        return TrackInteractorImpl(getMoviesRepository())
    }
}