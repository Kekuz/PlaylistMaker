package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.repository.AndroidMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.example.playlistmaker.domain.player.api.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.settings.api.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get(), androidContext ())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    factory<MediaPlayerRepository> {
        AndroidMediaPlayerRepositoryImpl(get(), get())
    }
}