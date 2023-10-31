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
        viewModel = ViewModelProvider(
            this,
            TrackFactory(intent.extras?.getString("track")!!)
        )[AudioPlayerViewModel::class.java]

        bindViews()
        addObservers()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    private fun bindViews(){
        with(binding) {
            playButton.setOnClickListener {
                viewModel.playbackControl()
            }

            ivBackArrowBtn.setOnClickListener {
                finish()
            }

            nameTv.text = viewModel.track.trackName
            authorTv.text = viewModel.track.artistName
            trackTimeValueTv.text = viewModel.trackTimeFormat

            //В задании написано: "Показывать название альбома (collectionName) (если есть)"
            //iTunes при отсутствии альбома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
            if (viewModel.track.collectionName.endsWith(NO_ALBUM_SUBSTRING)) {
                albumGroup.isVisible = false
            } else {
                albumValueTv.text = viewModel.track.collectionName
            }
            yearValueTv.text = viewModel.track.releaseDate.substringBefore('-')
            genreValueTv.text = viewModel.track.primaryGenreName
            countryValueTv.text = viewModel.track.country
        }

        Glide.with(this)
            .load(viewModel.artworkUrl512)
            .placeholder(R.drawable.big_trackplaceholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(binding.artworkUrl100)

    }

    private fun addObservers(){
        with(viewModel) {
            currentTimeLiveData.observe(this@AudioPlayer) {
                binding.currentTimeTv.text = it
            }

            playButtonImageLiveData.observe(this@AudioPlayer) {
                binding.playButton.setImageResource(it)
            }
        }
    }
    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 30
        private const val NO_ALBUM_SUBSTRING = " - Single"
    }
}