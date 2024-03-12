package com.example.playlistmaker.di

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.favorite.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.media.view_model.MediaViewModel
import com.example.playlistmaker.ui.new_playlist.view_model.NewPlaylistViewModel
import com.example.playlistmaker.ui.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {


    viewModel { (track: Track) ->
        AudioPlayerViewModel(track, get(), get())
    }

    viewModel {
        MediaViewModel()
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }

    viewModel {
        PlaylistsViewModel(get(), get())
    }

}