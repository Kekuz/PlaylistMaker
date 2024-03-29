package com.example.playlistmaker.ui.playlist.view_model


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
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistDeletingInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import com.example.playlistmaker.ui.playlist.model.PlaylistViewState
import com.example.playlistmaker.ui.util.Debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

class PlaylistViewModel(
    private val id: Int,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val playlistDeletingInteractor: PlaylistDeletingInteractor,
    private val favoritesInteractor: FavoritesInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistViewState>()

    fun observeState(): LiveData<PlaylistViewState> = stateLiveData

    private var playlist: Playlist? = null
    private val tracks = mutableListOf<Track>()
    var bottomSheetHeight: Int? = null

    fun clickDebounce(): Boolean = Debounce().clickDebounce()

    fun getPlaylist() {
        viewModelScope.launch {
            playlist = playlistInteractor.getPlaylistById(id)
            tracks.clear()
            tracks.addAll(
                playlistInteractor.getTracksFromPlaylistByIds(
                    playlist?.trackIdsList ?: emptyList()
                )
            )
            markFavoriteTracks()

            playlist?.let {
                stateLiveData.postValue(PlaylistViewState.PlaylistContent(it, tracks))
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlist?.let {
                playlistDeletingInteractor.deleteTrackFromPlaylist(track.trackId.toString(), it)
                tracks.remove(track)
                stateLiveData.postValue(
                    PlaylistViewState.PlaylistContentDeleteTrack(
                        track,
                        playlist!!.tracksCount - 1
                    )
                )
            }
            playlist = playlistInteractor.getPlaylistById(id)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlist?.let { playlistDeletingInteractor.deletePlaylist(it) }
        }
    }

    fun getTracksCount(): Int {
        return playlist?.tracksCount ?: 0
    }

    fun getTrackId(): Int {
        return playlist?.id ?: -1
    }

    fun getCurrentPlaylist(): Playlist {
        return playlist!!
    }

    fun getCurrentTracks(): List<Track> {
        return tracks
    }

    fun countPlaylistMinutes(): Int {
        val secondsSum =
            tracks.sumOf {
                it.trackTime.substring(it.trackTime.indexOf(":") + 1).toInt()
            }
        val minutesSum =
            tracks.sumOf {
                it.trackTime.substring(0, it.trackTime.indexOf(":"))
                    .toInt() * TimeUnit.MINUTES.toSeconds(1).toInt()
            }
        return (secondsSum + minutesSum) / TimeUnit.MINUTES.toSeconds(1).toInt()
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
            playlistCoverInteractor.getImageFromPrivateStorage(playlist?.pathToCover ?: "")
                .toUri().path
        )
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