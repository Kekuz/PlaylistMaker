package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.playlist.model.PlaylistViewState
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.util.Convert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!


    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(
            requireArguments().getInt("id")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOnClick()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistViewState) {
        when (state) {
            is PlaylistViewState.PlaylistContent -> showPlaylistContent(
                state.playlists,
                state.tracks
            )

            is PlaylistViewState.BottomViewContent -> showBottomViewContent()
        }
    }

    private fun showBottomViewContent() = with(binding) {
        //TODO("Будет сделано в будущем")
    }

    private fun showPlaylistContent(playlist: Playlist, tracks: List<Track>) = with(binding) {
        Glide.with(requireView()).load(viewModel.getFileImageFromStorage()).centerCrop()
            .into(ivPlaylistPic)

        tvPlaylistName.text = playlist.name
        if (playlist.description != "") {
            tvPlaylistDescription.text = playlist.description
        } else {
            tvPlaylistDescription.isVisible = false
        }
        val secondsSum =
            tracks.sumOf {
                it.trackTime.substring(it.trackTime.indexOf(":") + 1).toInt()
            }
        val minutesSum =
            tracks.sumOf {
                it.trackTime.substring(0, it.trackTime.indexOf(":")).toInt() * 60
            }
        val totalTime =
                (secondsSum + minutesSum) / 60


        tvPlaylistDuration.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_minutes, totalTime, totalTime
        )

        tvPlaylistTrackCount.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_track, playlist.tracksCount, playlist.tracksCount
        )

        BottomSheetBehavior.from(bottomSheet)
            .setPeekHeight(
                calculateBottomSheetHeight(playlist.description)
            )
    }

    private fun calculateBottomSheetHeight(description: String?): Int {
        with(binding) {
            val screenHeight = coordinator.height
            val picHeight = ivPlaylistPic.height
            val playlistNameHeight = tvPlaylistName.height + tvPlaylistName.marginTop
            val playlistDescriptionHeight = if (description.isNullOrEmpty()) {
                0
            } else {
                tvPlaylistDescription.height + tvPlaylistDescription.marginTop
            }

            val playlistTrackCountHeight =
                tvPlaylistTrackCount.height + tvPlaylistTrackCount.marginTop
            val shareHeight = ibShare.height + ibShare.marginTop + ibShare.marginBottom

            return screenHeight -
                    picHeight -
                    playlistNameHeight -
                    playlistDescriptionHeight -
                    playlistTrackCountHeight -
                    shareHeight
        }

    }

    private fun bindOnClick() = with(binding) {
        toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}