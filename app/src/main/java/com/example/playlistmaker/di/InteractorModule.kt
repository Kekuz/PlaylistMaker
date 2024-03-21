package com.example.playlistmaker.di

import com.example.playlistmaker.domain.favorites.api.interactor.FavoritesInteractor
import com.example.playlistmaker.domain.favorites.impl.FavoritesInteractorImpl
import com.example.playlistmaker.domain.player.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistDeletingInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.impl.PlaylistCoverInteractorImpl
import com.example.playlistmaker.domain.playlist.impl.PlaylistDeletingInteractorImpl
import com.example.playlistmaker.domain.playlist.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.api.interactor.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.api.interactor.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single<PlaylistCoverInteractor> {
        PlaylistCoverInteractorImpl(get())
    }

    single<PlaylistDeletingInteractor> {
        PlaylistDeletingInteractorImpl(get())
    }
}