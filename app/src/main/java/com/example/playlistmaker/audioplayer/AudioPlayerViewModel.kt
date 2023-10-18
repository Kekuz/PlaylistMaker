package com.example.playlistmaker.audioplayer

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(val track: Track) : ViewModel() {

    val currentTimeLiveData = MutableLiveData<String>()
    val playButtonIsEnabledLiveData = MutableLiveData<Boolean>()
    val playButtonImageLiveData = MutableLiveData<Int>()

    val trackTimeFormat: String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val dateFormat = SimpleDateFormat("m:ss", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())

    init {
        preparePlayer()
        Log.e("Track info", track.toString())
        playButtonIsEnabledLiveData.value = false
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    private val trackTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                currentTimeLiveData.value = dateFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_REFRESH_DELAY)
                Log.e("Timer", mediaPlayer.currentPosition.toString())
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButtonIsEnabledLiveData.value = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButtonImageLiveData.value = IMAGE_BUTTON_STATE_PLAY
            playerState = STATE_PREPARED
            currentTimeLiveData.value = CURRENT_TIME_ZERO
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButtonImageLiveData.value = IMAGE_BUTTON_STATE_PAUSE
        playerState = STATE_PLAYING
        trackTimerRunnable.run()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playButtonImageLiveData.value = IMAGE_BUTTON_STATE_PLAY
        playerState = STATE_PAUSED
        handler.removeCallbacks(trackTimerRunnable)
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                Log.e("State", "pause")
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                Log.e("State", "play")
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

        const val IMAGE_BUTTON_STATE_PLAY = 0
        const val IMAGE_BUTTON_STATE_PAUSE = 1

        const val CURRENT_TIME_ZERO = "zero"
    }


}