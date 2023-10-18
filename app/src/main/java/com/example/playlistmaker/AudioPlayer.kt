package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private val dateFormat by lazy { SimpleDateFormat("m:ss", Locale.getDefault()) }
    private val handler = Handler(Looper.getMainLooper())

    private val trackTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                binding.currentTimeTv.text = dateFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_REFRESH_DELAY)
                Log.e("Timer", mediaPlayer.currentPosition.toString())
            }
        }

    }

    private lateinit var track: Track
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playButton.isEnabled = false

        val json = intent.extras?.getString("track")
        track = Gson().fromJson(json, Track::class.java)

        preparePlayer()

        binding.playButton.setOnClickListener {
            playbackControl()
        }

        binding.ivBackArrowBtn.setOnClickListener {
            finish()
        }

        Log.e("trackInfo", track.toString())
        binding.nameTv.text = track.trackName
        binding.authorTv.text = track.artistName
        binding.trackTimeValueTv.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        //В задании написано: "Показывать название альбома (collectionName) (если есть)"
        //iTunes при отсутствии альбома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
        if (track.collectionName.endsWith(NO_ALBUM_SUBSTRING)) {
            binding.albumGroup.isVisible = false
        } else {
            binding.albumValueTv.text = track.collectionName
        }
        binding.yearValueTv.text = track.releaseDate.substringBefore('-')
        binding.genreValueTv.text = track.primaryGenreName
        binding.countryValueTv.text = track.country

        val artworkUrl512 =
            track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.big_trackplaceholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(binding.artworkUrl100)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.audio_player_play_button)
            playerState = STATE_PREPARED
            binding.currentTimeTv.text =  R.string.mockup_audio_player_time.toString()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.audio_player_pause_button)
        playerState = STATE_PLAYING
        trackTimerRunnable.run()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.audio_player_play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(trackTimerRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                Log.e("Log", "pause")
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                Log.e("Log", "Play")
                startPlayer()
            }
        }
    }

    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 30
        private const val NO_ALBUM_SUBSTRING = " - Single"

        private const val TIMER_REFRESH_DELAY = 300L

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}