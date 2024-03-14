package com.example.playlistmaker.ui.playlists.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.ui.model.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()

    fun observeState(): LiveData<PlaylistState> = stateLiveData
    fun getPlaylists() {
        viewModelScope.launch {
            val playlists = playlistRepository.getPlaylists()
            Log.e("playlists", playlists.toString())
            if (playlists.isEmpty()) {
                stateLiveData.postValue(PlaylistState.Empty)
            } else {
                stateLiveData.postValue(PlaylistState.Content(playlists))
            }
        }
    }

    fun getCoverRepository(): PlaylistRepository {
        return playlistRepository
    }
}