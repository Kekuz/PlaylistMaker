package com.example.playlistmaker.ui.edit_playlist.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.ui.edit_playlist.model.EditPlaylistViewState
import com.example.playlistmaker.ui.new_playlist.view_model.NewPlaylistViewModel
import kotlinx.coroutines.launch
import java.io.File

class EditPlaylistViewModel(
    private val id: Int,
    private val playlistRepository: PlaylistRepository
) : NewPlaylistViewModel(playlistRepository) {

    private val stateLiveData = MutableLiveData<EditPlaylistViewState>()

    fun observeState(): LiveData<EditPlaylistViewState> = stateLiveData

    private lateinit var playlist: Playlist

    init {
        getPlaylist()
    }

    private fun getPlaylist() {
        viewModelScope.launch {
            playlist = playlistRepository.getPlaylistById(id)!!

            playlist?.let {
                stateLiveData.postValue(EditPlaylistViewState.Content(it))
            }
        }
    }

    fun updatePlaylist(
        name: String,
        description: String,
        pathToCover: String? = null
    ) {
        viewModelScope.launch {
            playlistRepository.updatePlaylist(
                if (pathToCover == null) {
                    playlist.copy(
                        name = name,
                        description = description,
                    )
                } else {
                    playlist.copy(
                        name = name,
                        description = description,
                        pathToCover = pathToCover
                    )
                }

            )
        }
    }

    fun getCoverFile(): File =
        File(playlistRepository.getImageFromPrivateStorage(playlist.pathToCover).toUri().path)

}