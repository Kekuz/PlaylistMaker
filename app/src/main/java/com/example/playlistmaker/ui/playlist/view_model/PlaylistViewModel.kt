package com.example.playlistmaker.ui.playlist.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.ui.playlist.model.PlaylistViewState
import kotlinx.coroutines.launch
import java.io.File

class PlaylistViewModel(
    private val id: Int,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistViewState>()

    fun observeState(): LiveData<PlaylistViewState> = stateLiveData

    private var playlist: Playlist? = null
    private val tracks = mutableListOf<Track>()

    init {
        getPlaylist()
    }

    private fun getPlaylist() {
        viewModelScope.launch {
            playlist = playlistRepository.getPlaylistById(id)
            tracks.addAll(
                playlistRepository.getTracksFromPlaylistByIds(
                    playlist?.trackIdsList ?: emptyList()
                )
            )

            playlist?.let {
                stateLiveData.postValue(PlaylistViewState.PlaylistContent(it, tracks))
            }
        }
    }

    fun getFileImageFromStorage(): File {
        return File(
            playlistRepository.getImageFromPrivateStorage(playlist?.name ?: "").toUri().path
        )
    }
}