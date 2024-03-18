package com.example.playlistmaker.ui.playlist.view_model

import android.util.Log
import androidx.core.net.toUri
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.favorites.api.interactor.FavoritesInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.ui.playlist.model.PlaylistViewState
import com.example.playlistmaker.ui.util.Debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

class PlaylistViewModel(
    private val id: Int,
    private val playlistRepository: PlaylistRepository,
    private val favoritesInteractor: FavoritesInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistViewState>()

    fun observeState(): LiveData<PlaylistViewState> = stateLiveData

    private var playlist: Playlist? = null
    private val tracks = mutableListOf<Track>()
    var bottomSheetHeight: Int? = null

    init {
        getPlaylist()
    }

    fun clickDebounce(): Boolean = Debounce().clickDebounce()

    private fun getPlaylist() {
        viewModelScope.launch {
            playlist = playlistRepository.getPlaylistById(id)
            tracks.addAll(
                playlistRepository.getTracksFromPlaylistByIds(
                    playlist?.trackIdsList ?: emptyList()
                )
            )
            markFavoriteTracks()

            playlist?.let {
                stateLiveData.postValue(PlaylistViewState.PlaylistContent(it, tracks))
            }
        }
    }

    fun countPlaylistMinutes(): Int {
        val secondsSum =
            tracks.sumOf {
                it.trackTime.substring(it.trackTime.indexOf(":") + 1).toInt()
            }
        val minutesSum =
            tracks.sumOf {
                it.trackTime.substring(0, it.trackTime.indexOf(":")).toInt() * 60
            }
        return (secondsSum + minutesSum) / 60
    }

    private suspend fun markFavoriteTracks() {
        favoritesInteractor.getFavorites().map { list ->
            list.map { it.trackId }
        }.collect { favoriteList ->
            tracks.forEach {
                if (favoriteList.contains(it.trackId)) {
                    it.isFavorite = true
                }
            }
        }
    }

    fun getFileImageFromStorage(): File {
        return File(
            playlistRepository.getImageFromPrivateStorage(playlist?.name ?: "").toUri().path
        )
    }

    fun getCoverRepository(): PlaylistRepository {
        return playlistRepository
    }

    fun calculateBottomSheetHeight(binding: FragmentPlaylistBinding) {
        if (bottomSheetHeight == null) {
            with(binding) {
                val screenHeight = coordinator.height
                val picHeight = ivPlaylistPic.height
                val playlistNameHeight = tvPlaylistName.height + tvPlaylistName.marginTop
                val playlistDescriptionHeight = if (playlist?.description.isNullOrEmpty()) {
                    0
                } else {
                    tvPlaylistDescription.height + tvPlaylistDescription.marginTop
                }

                val playlistTrackCountHeight =
                    tvPlaylistTrackCount.height + tvPlaylistTrackCount.marginTop
                val shareHeight = ibShare.height + ibShare.marginTop + ibShare.marginBottom
                bottomSheetHeight = screenHeight -
                        picHeight -
                        playlistNameHeight -
                        playlistDescriptionHeight -
                        playlistTrackCountHeight -
                        shareHeight
            }
        }
    }
}