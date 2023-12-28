package com.example.playlistmaker.ui.audioplayer.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.api.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerStates
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerViewState
import com.example.playlistmaker.ui.audioplayer.models.PlayerView

class AudioPlayerViewModel(
    private val track: Track,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<AudioPlayerViewState>()
    fun observeState(): LiveData<AudioPlayerViewState> = stateLiveData

    private val playerView = PlayerView(CURRENT_TIME_ZERO, false)

    private var activityOrientation : Int? = null
    fun setActivityOrientation(orientation: Int) {
        activityOrientation = orientation
    }
    fun getActivityOrientation(): Int = activityOrientation!!

    init {
        preparePlayer()
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
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

    fun pausePlayer(){
        mediaPlayerInteractor.pausePlayer()
    }

    private fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    fun playBackControl(){
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
                handler.postDelayed(this, TIMER_REFRESH_DELAY_MILLIS)
                Log.d("Timer", currentTimer)
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
        const val TIMER_REFRESH_DELAY_MILLIS = 250L
        const val CURRENT_TIME_ZERO = "0:00"
    }

}