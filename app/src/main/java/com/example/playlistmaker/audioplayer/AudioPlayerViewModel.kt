package com.example.playlistmaker.audioplayer

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.search.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(val track: Track) : ViewModel() {

    private val _currentTimeLiveData = MutableLiveData<String>()
    private val _playButtonImageLiveData = MutableLiveData<Int>()

    val currentTimeLiveData: LiveData<String> = _currentTimeLiveData
    val playButtonImageLiveData: LiveData<Int> = _playButtonImageLiveData

    val trackTimeFormat: String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val dateFormat = SimpleDateFormat("m:ss", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())

    init {
        preparePlayer()
        Log.d("Track info", track.toString())
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    private val trackTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                _currentTimeLiveData.value = dateFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_REFRESH_DELAY)
                Log.d("Timer", mediaPlayer.currentPosition.toString())
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            _playButtonImageLiveData.value = R.drawable.audio_player_play_button
            playerState = STATE_PREPARED
            _currentTimeLiveData.value = CURRENT_TIME_ZERO
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _playButtonImageLiveData.value = R.drawable.audio_player_pause_button
        playerState = STATE_PLAYING
        trackTimerRunnable.run()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        _playButtonImageLiveData.value = R.drawable.audio_player_play_button
        playerState = STATE_PAUSED
        handler.removeCallbacks(trackTimerRunnable)
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                Log.d("State", "pause")
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                Log.d("State", "play")
                startPlayer()
            }
        }
    }

    companion object {
        const val TIMER_REFRESH_DELAY = 300L

        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

        const val CURRENT_TIME_ZERO = "0:00"
    }


}