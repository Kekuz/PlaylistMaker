package com.example.playlistmaker.data.mediaplayer

import android.util.Log
import com.example.playlistmaker.domain.api.MediaPlayer
import com.example.playlistmaker.domain.models.PlayerStates
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


class AndroidMediaPlayerImpl(private val track : Track) : MediaPlayer {

    private val mediaPlayer = android.media.MediaPlayer()
    private var playerState = PlayerStates.STATE_DEFAULT

    private val dateFormat = SimpleDateFormat("m:ss", Locale.getDefault())

    override fun prepareMediaPlayer(consumer: () -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStates.STATE_PREPARED
            Log.d("State", "prepared")
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStates.STATE_PREPARED
            Log.d("State", "prepared")
            consumer.invoke()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStates.STATE_PLAYING
        Log.d("State", "play")
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStates.STATE_PAUSED
        Log.d("State", "pause")
    }

    override fun playbackControl(consumer: (PlayerStates) -> Unit) {
        when (playerState) {
            PlayerStates.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerStates.STATE_DEFAULT -> {/*Что???*/}
        }
        consumer.invoke(playerState)
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlayerStatePlaying(): Boolean {
        return playerState == PlayerStates.STATE_PLAYING
    }

    override fun getCurrentPosition(): String {
        return dateFormat.format(mediaPlayer.currentPosition)
    }
}