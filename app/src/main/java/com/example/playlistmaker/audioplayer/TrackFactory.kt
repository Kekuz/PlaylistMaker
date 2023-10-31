package com.example.playlistmaker.audioplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.Track
import com.google.gson.Gson

class TrackFactory(val string: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(Gson().fromJson(string, Track::class.java)) as T
    }
}