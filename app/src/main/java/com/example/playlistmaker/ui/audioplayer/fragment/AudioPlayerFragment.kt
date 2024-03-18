package com.example.playlistmaker.ui.audioplayer.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.databinding.SnackbarViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.audioplayer.bottom_sheet_recycler.BottomSheetAdapter
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerBottomSheetState
import com.example.playlistmaker.ui.audioplayer.models.AudioPlayerViewState
import com.example.playlistmaker.ui.audioplayer.models.PlayerView
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.util.Convert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireArguments().getParcelable(ARGS_TRACK, Track::class.java)
            } else {
                requireArguments().getParcelable(ARGS_TRACK)
            }
        )
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var playlistAdapter: BottomSheetAdapter

    private val onClick: (Playlist) -> Unit = {
        viewModel.addTrackToPlaylist(playlist = it)
    }

    private var _activityOrientation: Int? = null
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

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = BottomSheetAdapter(viewModel.getCoverRepository(), onClick)
        binding.rvPlaylists.adapter = playlistAdapter

        viewModel.loadView()
        bindClickListeners()
        bindBottomSheet()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeBottomSheetState().observe(viewLifecycleOwner) {
            renderBottomSheet(it)
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

        likeButton.setOnClickListener {
            viewModel.onFavoriteClick()
        }

        audioPlayerToolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        playlistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }
    }

    private fun bindBottomSheet() = with(binding) {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // newState — новое состояние BottomSheet
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewModel.getPlaylists()
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.isVisible = false
                    }

                    else -> {
                        overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset + 1f
            }
        })
    }

    private fun render(state: AudioPlayerViewState) {
        when (state) {
            is AudioPlayerViewState.Content -> bindViews(state.track, state.player)
            is AudioPlayerViewState.Player -> bindCurrentPlayer(state.player)
        }
    }

    private fun renderBottomSheet(state: AudioPlayerBottomSheetState) {
        when (state) {
            is AudioPlayerBottomSheetState.ContentPlaylists -> showContent(state.playlists)
            is AudioPlayerBottomSheetState.EmptyPlaylists -> showEmptyContent()
            is AudioPlayerBottomSheetState.TrackAdded -> trackAdded(state.playlist)
            is AudioPlayerBottomSheetState.TrackAlreadyExist -> trackAlreadyExist(state.playlist)
        }
    }

    private fun trackAdded(playlist: Playlist) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        makeSnackbar(requireView(), playlist.name, false).show()
    }

    private fun trackAlreadyExist(playlist: Playlist) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        makeSnackbar(requireView(), playlist.name, true).show()
    }

    private fun showEmptyContent() = with(binding) {
        //emptyGroup.isVisible = true
        rvPlaylists.isVisible = false
        playlistAdapter.clearPlaylists()
    }

    private fun showContent(playlists: List<Playlist>) = with(binding) {
        rvPlaylists.isVisible = true
        //emptyGroup.isVisible = false
        playlistAdapter.clearPlaylists()
        playlistAdapter.addPlaylists(playlists)
        playlistAdapter.notifyDataSetChanged()
    }


    private fun bindViews(track: Track, playerView: PlayerView) = with(binding) {
        nameTv.text = track.trackName
        authorTv.text = track.artistName
        trackTimeValueTv.text = track.trackTime

        if (track.isFavorite) {
            likeButton.setImageResource(R.drawable.audio_player_like_pressed_button)
        } else {
            likeButton.setImageResource(R.drawable.audio_player_like_button)
        }

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
            .transform(
                RoundedCorners(
                    Convert.dpToPx(
                        TRACK_ICON_CORNER_RADIUS,
                        requireContext()
                    )
                )
            )
            .into(binding.artworkUrl100)
    }

    private fun bindCurrentPlayer(playerView: PlayerView) = with(binding) {
        currentTimeTv.text = playerView.playTime

        if (playerView.playPicture)
            playButton.setImageResource(R.drawable.audio_player_pause_button)
        else
            playButton.setImageResource(R.drawable.audio_player_play_button)
    }

    @SuppressLint("RestrictedApi")
    private fun makeSnackbar(view: View, playlistName: String, isExist: Boolean): Snackbar {
        val customSnackbar = Snackbar.make(
            ContextThemeWrapper(requireContext(), R.style.CustomSnackbarTheme),
            view,
            "",
            Snackbar.LENGTH_LONG
        )
        val layout = customSnackbar.view as Snackbar.SnackbarLayout
        val bind: SnackbarViewBinding = SnackbarViewBinding.inflate(layoutInflater)

        layout.addView(bind.root, 0)
        if (isExist) {
            bind.sbText.text = "${getString(R.string.already_added_to_playlist)} $playlistName"
        } else {
            bind.sbText.text = "${getString(R.string.added_to_playlist)} $playlistName"
        }

        return customSnackbar
    }

    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 8f
        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)

    }
}