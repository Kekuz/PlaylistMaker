package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson

class TrackFactory(val string: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(Gson().fromJson(string, Track::class.java)) as T
    }
}