package com.example.playlistmaker.ui.new_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
) : ViewModel() {
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.savePlaylist(playlist)
        }
    }

    fun saveImage(uri: String, fileName: String) {
        playlistCoverInteractor.saveImageToPrivateStorage(uri, fileName)
    }
}