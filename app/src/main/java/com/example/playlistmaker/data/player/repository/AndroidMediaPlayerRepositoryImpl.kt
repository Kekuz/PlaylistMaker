package com.example.playlistmaker.data.player.repository

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.api.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.models.PlayerStates
import com.example.playlistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


class AndroidMediaPlayerRepositoryImpl(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
) : MediaPlayerRepository {

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

            PlayerStates.STATE_DEFAULT -> {
                //NOTHING TO DO
            }
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
        //TODO: Для этого моментика надо свой таймер сделать, а то это какая то шляпа
        return dateFormat.format(mediaPlayer.currentPosition)
    }
}