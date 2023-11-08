package com.example.playlistmaker.ui.audioplayer


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.audioplayer.TrackFactory
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding


class AudioPlayerActivity : AppCompatActivity() {
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
        bindClickListeners()
        addObservers()
    }

    override fun onPause() {
        super.onPause()
        viewModel.interactor.pausePlayer()
    }


    private fun bindViews() = with(binding) {

        nameTv.text = viewModel.track.trackName
        authorTv.text = viewModel.track.artistName
        trackTimeValueTv.text = viewModel.track.trackTime

        //В задании написано: "Показывать название альбома (collectionName) (если есть)"
        //iTunes при отсутствии альбома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
        if (viewModel.track.collectionName.endsWith(NO_ALBUM_SUBSTRING) || viewModel.track.collectionName == "-") {
            albumGroup.isVisible = false
        } else {
            albumValueTv.text = viewModel.track.collectionName
        }

        if(viewModel.track.releaseYear == "-"){
            yearGroup.isVisible = false
        }else{
            yearValueTv.text = viewModel.track.releaseYear
        }
        genreValueTv.text = viewModel.track.primaryGenreName
        countryValueTv.text = viewModel.track.country
        bindPicture()
    }

    private fun bindPicture() {
        Glide.with(this)
            .load(viewModel.track.artworkUrl512)
            .placeholder(R.drawable.big_trackplaceholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(binding.artworkUrl100)
    }

    private fun bindClickListeners() = with(binding) {
        playButton.setOnClickListener {
            viewModel.interactor.playbackControl {
                viewModel.playerButtonStateChanger(it)
            }
        }

        ivBackArrowBtn.setOnClickListener {
            finish()
        }
    }

    private fun addObservers() = with(viewModel) {

        currentTimeLiveData.observe(this@AudioPlayerActivity) {
            binding.currentTimeTv.text = it
        }

        playButtonImageLiveData.observe(this@AudioPlayerActivity) {
            binding.playButton.setImageResource(it)
        }
    }

    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 30
        private const val NO_ALBUM_SUBSTRING = " - Single"
    }
}