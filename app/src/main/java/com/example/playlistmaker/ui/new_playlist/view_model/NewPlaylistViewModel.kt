package com.example.playlistmaker.ui.new_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(playlist)
        }
    }

    fun saveImage(uri: String, fileName: String) {
        playlistRepository.saveImageToPrivateStorage(uri, fileName)
    }
}