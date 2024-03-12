package com.example.playlistmaker.ui.new_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistCoverRepository
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistRepository: PlaylistRepository,
    private val playlistCoverRepository: PlaylistCoverRepository
) : ViewModel() {
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(playlist)
        }
    }

    fun saveImage(uri: String, fileName: String) {
        playlistCoverRepository.saveImageToPrivateStorage(uri, fileName)
    }
}