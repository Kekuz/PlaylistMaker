package com.example.playlistmaker.ui.audioplayer.fragment

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerViewState
import com.example.playlistmaker.ui.audioplayer.models.PlayerView
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AudioPlayerViewModel by viewModel{
        parametersOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                requireArguments().getParcelable(ARGS_TRACK, Track::class.java)
            } else {
                requireArguments().getParcelable(ARGS_TRACK)
            }
        )
    }

    private var _activityOrientation : Int? = null
    private val activityOrientation get() = _activityOrientation!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityOrientation = requireActivity().requestedOrientation
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadView()
        bindClickListeners()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        //Плеер обычно ставится на паузу когда проходит resume
        //потому интерфейс обновляем
        binding.playButton.setImageResource(R.drawable.audio_player_play_button)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().requestedOrientation = activityOrientation
    }


    private fun bindClickListeners() = with(binding) {
        playButton.setOnClickListener {
            viewModel.playBackControl()
        }

        audioPlayerToolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
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

    private fun ImageView.setImage(artworkUrl512: String?) {
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
        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)

    }
}