package com.example.playlistmaker.audioplayer


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding


class AudioPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, TrackFactory(intent.extras?.getString("track")!!))[AudioPlayerViewModel::class.java]

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.ivBackArrowBtn.setOnClickListener {
            finish()
        }

        binding.nameTv.text = viewModel.track.trackName
        binding.authorTv.text = viewModel.track.artistName
        binding.trackTimeValueTv.text = viewModel.trackTimeFormat

        //В задании написано: "Показывать название альбома (collectionName) (если есть)"
        //iTunes при отсутствии альбома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
        if (viewModel.track.collectionName.endsWith(NO_ALBUM_SUBSTRING)) {
            binding.albumGroup.isVisible = false
        } else {
            binding.albumValueTv.text = viewModel.track.collectionName
        }
        binding.yearValueTv.text = viewModel.track.releaseDate.substringBefore('-')
        binding.genreValueTv.text = viewModel.track.primaryGenreName
        binding.countryValueTv.text = viewModel.track.country

        Glide.with(this)
            .load(viewModel.artworkUrl512)
            .placeholder(R.drawable.big_trackplaceholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(binding.artworkUrl100)

        viewModel.currentTimeLiveData.observe(this) {
            if (it != "zero"){
                binding.currentTimeTv.text = it
            } else {
                binding.currentTimeTv.text = getString(R.string.mockup_audio_player_time)
            }
        }

        viewModel.playButtonIsEnabledLiveData.observe(this) {
            binding.playButton.isEnabled = it
        }

        viewModel.playButtonImageLiveData.observe(this) {
            if(it == 0){
                binding.playButton.setImageResource(R.drawable.audio_player_play_button)
            }else if (it == 1){
                binding.playButton.setImageResource(R.drawable.audio_player_pause_button)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 30
        private const val NO_ALBUM_SUBSTRING = " - Single"
    }
}