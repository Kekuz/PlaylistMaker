package com.example.playlistmaker.ui.edit_playlist.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import com.example.playlistmaker.ui.edit_playlist.model.EditPlaylistViewState
import com.example.playlistmaker.ui.new_playlist.view_model.NewPlaylistViewModel
import kotlinx.coroutines.launch
import java.io.File

class EditPlaylistViewModel(
    private val id: Int,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
) : NewPlaylistViewModel(playlistInteractor, playlistCoverInteractor) {

    private val stateLiveData = MutableLiveData<EditPlaylistViewState>()

    fun observeState(): LiveData<EditPlaylistViewState> = stateLiveData

    private lateinit var playlist: Playlist

    init {
        getPlaylist()
    }

    private fun getPlaylist() {
        viewModelScope.launch {
            playlist = playlistInteractor.getPlaylistById(id)!!

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
            playlistInteractor.updatePlaylist(
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
        File(playlistCoverInteractor.getImageFromPrivateStorage(playlist.pathToCover).toUri().path)

}