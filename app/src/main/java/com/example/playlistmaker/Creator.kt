package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.repository.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.SearchHistoryStorage
import com.example.playlistmaker.data.repository.NightModeRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefNightModeStorage
import com.example.playlistmaker.data.storage.SharedPrefsSearchHistoryStorage
import com.example.playlistmaker.domain.api.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.interactor.NightModeInteractor
import com.example.playlistmaker.domain.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.api.repository.TrackRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.NightModeInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.models.Track

object Creator {

    private lateinit var appContext: Context
    private lateinit var trackToOpenPlayerActivity: Track
    fun initAppContext(context: Context) {
        appContext = context
    }

    fun initTrack(newTrack: Track) {
        trackToOpenPlayerActivity = newTrack
    }

    private fun getNightModeRepository(): NightModeRepository {
        return NightModeRepositoryImpl(SharedPrefNightModeStorage(appContext))
    }

    fun provideNightModeInteractor(): NightModeInteractor {
        return NightModeInteractorImpl(getNightModeRepository())
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    private fun getAndroidMediaPlayer(): MediaPlayerRepository {
        return AndroidMediaPlayerRepositoryImpl(trackToOpenPlayerActivity)
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getAndroidMediaPlayer())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(SharedPrefsSearchHistoryStorage(appContext))
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

}