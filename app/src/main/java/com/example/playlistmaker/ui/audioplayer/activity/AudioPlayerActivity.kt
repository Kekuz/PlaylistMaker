package com.example.playlistmaker.ui.audioplayer.activity


import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.models.TrackForPlayer
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerViewState
import com.example.playlistmaker.ui.audioplayer.models.PlayerView
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel{
        parametersOf(TrackForPlayer.get())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadView()
        bindClickListeners()

        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    private fun bindClickListeners() = with(binding) {
        playButton.setOnClickListener {
            viewModel.playBackControl()
        }

        audioPlayerToolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun render(state: AudioPlayerViewState) {
        when (state) {
            is AudioPlayerViewState.Content -> bindViews(state.track, state.player)
            is AudioPlayerViewState.Player -> bindCurrentPlayer(state.player)
        }

    }

    private fun bindViews(track: Track, playerView: PlayerView) = with(binding) {
        nameTv.text = track.trackName
        authorTv.text = track.artistName
        trackTimeValueTv.text = track.trackTime
        if (track.collectionName != "-") {
            albumValueTv.text = track.collectionName
            albumGroup.isVisible = true
        }
        if (track.releaseYear != "-") {
            yearValueTv.text = track.releaseYear
            yearGroup.isVisible = true
        }
        genreValueTv.text = track.primaryGenreName
        countryValueTv.text = track.country
        artworkUrl100.setImage(track.artworkUrl512)
        bindCurrentPlayer(playerView)
    }

    private fun ImageView.setImage(artworkUrl512: String) {
        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.big_trackplaceholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(binding.artworkUrl100)
    }

    private fun bindCurrentPlayer(playerView: PlayerView) = with(binding) {
        currentTimeTv.text = playerView.playTime

        if (playerView.playPicture)
            playButton.setImageResource(R.drawable.audio_player_pause_button)
        else
            playButton.setImageResource(R.drawable.audio_player_play_button)
    }

    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 30
    }
}