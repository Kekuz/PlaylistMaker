package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.NightModeRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefNightModeStorage
import com.example.playlistmaker.domain.api.interactor.NightModeInteractor
import com.example.playlistmaker.domain.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.api.repository.TrackRepository
import com.example.playlistmaker.domain.impl.NightModeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private lateinit var appContext: Context
    fun initAppContext(context: Context){
        appContext = context
    }
    private fun getNightModeRepository(): NightModeRepository {
        return NightModeRepositoryImpl(SharedPrefNightModeStorage(appContext))
    }

    fun provideNightModeInteractor(): NightModeInteractor {
        return NightModeInteractorImpl(getNightModeRepository())
    }
    private fun getMoviesRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): TrackInteractor {
        return TrackInteractorImpl(getMoviesRepository())
    }
}