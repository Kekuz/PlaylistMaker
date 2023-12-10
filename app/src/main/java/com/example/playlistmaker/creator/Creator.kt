package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.player.repository.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.settings.storage.SharedPrefSettingsStorage
import com.example.playlistmaker.data.search.storage.SharedPrefsSearchHistoryStorage
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.api.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.settings.api.interactor.SettingsInteractor
import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.settings.api.repository.SettingsRepository
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.api.ExternalNavigator
import com.example.playlistmaker.domain.sharing.api.interactor.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    private lateinit var appContext: Context
    private lateinit var trackToOpenPlayerActivity: Track
    fun initAppContext(context: Context) {
        appContext = context
    }

    fun initTrack(newTrack: Track) {
        trackToOpenPlayerActivity = newTrack
    }

    fun getTrack(): Track = trackToOpenPlayerActivity

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(SharedPrefSettingsStorage(appContext))
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(appContext)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(appContext), appContext)
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

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }

}