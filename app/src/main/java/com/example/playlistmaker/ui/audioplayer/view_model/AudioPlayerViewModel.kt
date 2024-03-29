package com.example.playlistmaker.ui.audioplayer.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.api.interactor.FavoritesInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.player.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerStates
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerBottomSheetState
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerViewState
import com.example.playlistmaker.ui.audioplayer.models.PlayerView
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: Track,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<AudioPlayerViewState>()
    fun observeState(): LiveData<AudioPlayerViewState> = stateLiveData

    private val stateBottomSheetLiveData = MutableLiveData<AudioPlayerBottomSheetState>()
    fun observeBottomSheetState(): LiveData<AudioPlayerBottomSheetState> = stateBottomSheetLiveData

    private val playerView = PlayerView(CURRENT_TIME_ZERO, false)

    init {
        preparePlayer()
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (playlist.trackIdsList.contains(track.trackId.toString())) {
            stateBottomSheetLiveData.postValue(
                AudioPlayerBottomSheetState.TrackAlreadyExist(
                    playlist
                )
            )
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist)
            }
            stateBottomSheetLiveData.postValue(
                AudioPlayerBottomSheetState.TrackAdded(
                    playlist
                )
            )
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            val playlists = playlistInteractor.getPlaylists()
            if (playlists.isEmpty()) {
                stateBottomSheetLiveData.postValue(AudioPlayerBottomSheetState.EmptyPlaylists)
            } else {
                stateBottomSheetLiveData.postValue(
                    AudioPlayerBottomSheetState.ContentPlaylists(
                        playlists
                    )
                )
            }
        }
    }

    fun getCoverInteractor(): PlaylistCoverInteractor {
        return playlistCoverInteractor
    }

    fun onFavoriteClick() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoritesInteractor.deleteFavorite(track)
            } else {
                favoritesInteractor.saveFavorite(track)
            }
        }
        track.isFavorite = !track.isFavorite
        loadView()
    }

    fun loadView() {
        stateLiveData.value = AudioPlayerViewState.Content(track, playerView)
    }

    private fun preparePlayer() {
        mediaPlayerInteractor.prepareMediaPlayer {
            playerView.playTime = CURRENT_TIME_ZERO
            playerView.playPicture = false
            stateLiveData.value = AudioPlayerViewState.Player(playerView)
        }
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    private fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    private fun trackEndingCheck() {
        mediaPlayerInteractor.trackEndingCheck {
            playerView.playTime = CURRENT_TIME_ZERO
            playerView.playPicture = false
            stateLiveData.value = AudioPlayerViewState.Player(playerView)
        }
    }

    fun playBackControl() {
        mediaPlayerInteractor.playbackControl {
            playerButtonStateChanger(it)
        }
    }


    private val trackTimerRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayerInteractor.isPlayerStatePlaying()) {
                val currentTimer = mediaPlayerInteractor.getCurrentPosition()
                playerView.playTime = currentTimer
                stateLiveData.value = AudioPlayerViewState.Player(playerView)
                trackEndingCheck()
                handler.postDelayed(this, mediaPlayerInteractor.getTimerRefreshDelayMillis())
                //Log.d("Timer", currentTimer)
            }
        }
    }

    private fun playerButtonStateChanger(playerState: PlayerStates) {
        when (playerState) {
            PlayerStates.STATE_PLAYING -> {
                playerView.playPicture = true
                stateLiveData.value = AudioPlayerViewState.Player(playerView)
                trackTimerRunnable.run()
            }

            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                playerView.playPicture = false
                stateLiveData.value = AudioPlayerViewState.Player(playerView)
                handler.removeCallbacks(trackTimerRunnable)
            }

            PlayerStates.STATE_DEFAULT -> {
                //NOTHING TO DO
            }
        }
    }

    companion object {
        const val CURRENT_TIME_ZERO = "0:00"
    }

}