package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.mediaplayer.AndroidMediaPlayerImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.NightModeRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefNightModeStorage
import com.example.playlistmaker.domain.api.MediaPlayer
import com.example.playlistmaker.domain.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.interactor.NightModeInteractor
import com.example.playlistmaker.domain.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.api.repository.TrackRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.NightModeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.models.Track

object Creator {

    private lateinit var appContext: Context
    private lateinit var track: Track
    fun initAppContext(context: Context){
        appContext = context
    }

    fun initTrack(newTrack: Track){
        track = newTrack
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

    private fun getAndroidMediaPlayer(): MediaPlayer{
        return AndroidMediaPlayerImpl(track)
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor{
        return MediaPlayerInteractorImpl(getAndroidMediaPlayer())
    }
}