package com.example.playlistmaker.ui.audioplayer.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerStates
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerViewState
import com.example.playlistmaker.ui.audioplayer.models.PlayerView

class AudioPlayerViewModel(
    private val track: Track,
    val mediaPlayerInteractor: MediaPlayerInteractor,
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<AudioPlayerViewState>()
    fun observeState(): LiveData<AudioPlayerViewState> = stateLiveData

    private val player = PlayerView(CURRENT_TIME_ZERO, false)

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun loadView() {
        stateLiveData.value = AudioPlayerViewState.Content(track, player)
    }

    private fun preparePlayer() {
        mediaPlayerInteractor.prepareMediaPlayer {
            player.playTime = CURRENT_TIME_ZERO
            player.playPicture = false
            stateLiveData.value = AudioPlayerViewState.Player(player)
        }
    }

    private fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }


    private val trackTimerRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayerInteractor.isPlayerStatePlaying()) {
                val currentTimer = mediaPlayerInteractor.getCurrentPosition()
                player.playTime = currentTimer
                stateLiveData.value = AudioPlayerViewState.Player(player)
                handler.postDelayed(this, TIMER_REFRESH_DELAY)
                Log.d("Timer", currentTimer)
            }
        }
    }

    fun playerButtonStateChanger(playerState: PlayerStates) {
        when (playerState) {
            PlayerStates.STATE_PLAYING -> {
                player.playPicture = true
                stateLiveData.value = AudioPlayerViewState.Player(player)
                trackTimerRunnable.run()
            }

            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                player.playPicture = false
                stateLiveData.value = AudioPlayerViewState.Player(player)
                handler.removeCallbacks(trackTimerRunnable)

            }

            PlayerStates.STATE_DEFAULT -> {/*Что?*/
            }
        }
    }

    companion object {
        const val TIMER_REFRESH_DELAY = 250L
        const val CURRENT_TIME_ZERO = "0:00"

        fun getViewModelFactory(
            track: Track,
            mediaPlayerInteractor: MediaPlayerInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(track, mediaPlayerInteractor)
            }
        }
    }

}