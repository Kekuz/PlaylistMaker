package com.example.playlistmaker.ui.new_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.domain.playlist.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(playlist)
        }
    }
}