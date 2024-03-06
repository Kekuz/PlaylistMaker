package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.media.view_model.MediaViewModel
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

}