package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.audioplayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.playlist.model.PlaylistViewState
import com.example.playlistmaker.ui.playlist.recycler.BottomSheetPlaylistAdapter
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!


    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(
            requireArguments().getInt("id")
        )
    }

    private lateinit var tracksAdapter: BottomSheetPlaylistAdapter

    private val onClick: (Track) -> Unit = {
        if (viewModel.clickDebounce()) {
            if (it.previewUrl != "-") {
                Log.d("Track opened", it.toString())
                findNavController().navigate(
                    R.id.action_playlistFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(it)
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_music_on_server),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksAdapter = BottomSheetPlaylistAdapter(viewModel.getCoverRepository(), onClick)

        binding.rvTracks.adapter = tracksAdapter

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
        }
    }

    private fun showBottomViewContent(tracks: List<Track>) {
        tracksAdapter.clearTracks()
        Log.e("Tracks", tracks.toString())
        tracksAdapter.addTracks(tracks)
        tracksAdapter.notifyDataSetChanged()
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

        val totalTime = viewModel.countPlaylistMinutes()
        tvPlaylistDuration.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_minutes,
            totalTime,
            totalTime
        )

        tvPlaylistTrackCount.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_track, playlist.tracksCount, playlist.tracksCount
        )

        viewModel.calculateBottomSheetHeight(this)
        bottomSheetPickHeight()

        showBottomViewContent(tracks)
    }

    private fun bottomSheetPickHeight() {
        viewModel.bottomSheetHeight?.let {
            BottomSheetBehavior.from(binding.bottomSheet).peekHeight = it
        }
    }


    private fun bindOnClick() = with(binding) {
        toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}