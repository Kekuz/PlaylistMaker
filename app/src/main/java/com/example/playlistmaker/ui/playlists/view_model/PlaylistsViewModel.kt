package com.example.playlistmaker.ui.playlists.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import com.example.playlistmaker.ui.playlists.model.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()

    fun observeState(): LiveData<PlaylistsState> = stateLiveData
    fun getPlaylists() {
        viewModelScope.launch {
            val playlists = playlistInteractor.getPlaylists()
            Log.d("playlists", playlists.toString())
            if (playlists.isEmpty()) {
                stateLiveData.postValue(PlaylistsState.Empty)
            } else {
                stateLiveData.postValue(PlaylistsState.Content(playlists))
            }
        }
    }

    fun getCoverInteractor(): PlaylistCoverInteractor {
        return playlistCoverInteractor
    }
}