package com.example.playlistmaker.ui.audioplayer.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.models.PlayerStates
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerViewModel(val track: Track) : ViewModel() {

    val interactor = Creator.provideMediaPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val _currentTimeLiveData = MutableLiveData<String>()
    private val _playButtonImageLiveData = MutableLiveData<Int>()

    val currentTimeLiveData: LiveData<String> = _currentTimeLiveData
    val playButtonImageLiveData: LiveData<Int> = _playButtonImageLiveData

    init {
        interactor.prepareMediaPlayer {
            _playButtonImageLiveData.value = R.drawable.audio_player_play_button
            _currentTimeLiveData.value = CURRENT_TIME_ZERO
        }
    }

    override fun onCleared() {
        super.onCleared()
        interactor.releasePlayer()
    }

    private val trackTimerRunnable = object : Runnable {
        override fun run() {
            if (interactor.isPlayerStatePlaying()) {
                val currentTimer = interactor.getCurrentPosition()
                _currentTimeLiveData.value = currentTimer
                handler.postDelayed(this, TIMER_REFRESH_DELAY)
                Log.d("Timer", currentTimer)
            }
        }
    }

    fun playerButtonStateChanger(playerState: PlayerStates) {
        when (playerState) {
            PlayerStates.STATE_PLAYING -> {
                _playButtonImageLiveData.value = R.drawable.audio_player_pause_button
                trackTimerRunnable.run()
            }

            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                _playButtonImageLiveData.value = R.drawable.audio_player_play_button
                handler.removeCallbacks(trackTimerRunnable)

            }
            PlayerStates.STATE_DEFAULT -> {/*Что?*/}
        }
    }

    companion object {
        const val TIMER_REFRESH_DELAY = 300L
        const val CURRENT_TIME_ZERO = "0:00"
    }

}