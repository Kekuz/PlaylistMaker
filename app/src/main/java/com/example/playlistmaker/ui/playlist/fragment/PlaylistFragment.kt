package com.example.playlistmaker.ui.playlist.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistDetailsBinding
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.audioplayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.edit_playlist.fragment.EditPlaylistFragment
import com.example.playlistmaker.ui.playlist.model.PlaylistViewState
import com.example.playlistmaker.ui.playlist.recycler.BottomSheetPlaylistAdapter
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.StringBuilder


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

    private val onLongClick: (Track) -> Unit = {
        makeDeleteTrackDialog(it).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksAdapter =
            BottomSheetPlaylistAdapter(onClick, onLongClick)

        binding.rvTracks.adapter = tracksAdapter

        bindOnClick()
        //bindBottomSheetDetails()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylist()
        /*if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            binding.overlay.isVisible = true
            binding.overlay.alpha = 1f
        }*/
    }

    private fun render(state: PlaylistViewState) {
        when (state) {
            is PlaylistViewState.PlaylistContent -> showPlaylistContent(
                state.playlists,
                state.tracks
            )

            is PlaylistViewState.PlaylistContentDeleteTrack -> showBottomViewContentAfterDeleting(
                state.track,
                state.tracksCount
            )
        }
    }

    private fun showBottomViewContent(tracks: List<Track>) {
        viewModel.calculateBottomSheetHeight(binding)
        bottomSheetPickHeight()
        binding.tvEmptyTracks.isVisible = false

        tracksAdapter.clearTracks()
        Log.d("Tracks", tracks.toString())
        tracksAdapter.addTracks(tracks.reversed())
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showBottomEmptyContent() {
        viewModel.calculateBottomSheetHeight(binding)
        bottomSheetPickHeight()
        tracksAdapter.clearTracks()
        tracksAdapter.notifyDataSetChanged()
        binding.tvEmptyTracks.isVisible = true

    }

    private fun showBottomViewContentAfterDeleting(track: Track, tracksCount: Int) {
        val deletedTrackIndex = tracksAdapter.removeTrack(track)
        Log.d("Tracks deleted", track.toString())
        binding.tvEmptyTracks.isVisible = tracksAdapter.itemCount == 0

        tracksAdapter.notifyItemRemoved(deletedTrackIndex)
        tracksAdapter.notifyItemRangeChanged(deletedTrackIndex, tracksAdapter.itemCount)

        bindDurationAndTrackCount(tracksCount)
    }

    private fun showPlaylistContent(playlist: Playlist, tracks: List<Track>) = with(binding) {
        Glide.with(requireView()).load(viewModel.getFileImageFromStorage()).centerCrop()
            .into(ivPlaylistPic)

        tvPlaylistName.text = playlist.name
        if (playlist.description != "") {
            tvPlaylistDescription.text = playlist.description
            tvPlaylistDescription.isVisible = true
        } else {
            tvPlaylistDescription.isVisible = false
        }

        bindDurationAndTrackCount(playlist.tracksCount)

        ibShare.setOnClickListener {
            makeConditionShareIntent(playlist, tracks)
        }

        if (tracks.isNotEmpty()) {
            showBottomViewContent(tracks)
        } else {
            showBottomEmptyContent()
        }

    }

    private fun makeConditionShareIntent(playlist: Playlist, tracks: List<Track>) {
        if (viewModel.getTracksCount() == 0) {
            makeSnackbar(requireView()).show()
        } else {
            makeShareIntent(playlist, tracks)
        }
    }

    private fun bindDurationAndTrackCount(tracksCount: Int) = with(binding) {
        val totalTime = viewModel.countPlaylistMinutes()
        tvPlaylistDuration.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_minutes,
            totalTime,
            totalTime
        )

        tvPlaylistTrackCount.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_track, tracksCount, tracksCount
        )

    }

    private fun bottomSheetPickHeight() {
        viewModel.bottomSheetHeight?.let {
            BottomSheetBehavior.from(binding.bottomSheet).peekHeight = it
        }
    }

    private fun makeShareIntent(playlist: Playlist, tracks: List<Track>): Intent {
        val tracksString = StringBuilder()

        tracks.forEachIndexed { i, track ->
            tracksString.append("${i + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})\n")
        }


        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "${
                    playlist.name
                } \n${
                    playlist.description
                } \n${
                    requireContext().resources.getQuantityString(
                        R.plurals.plurals_track,
                        playlist.tracksCount,
                        playlist.tracksCount,
                    )
                } \n$tracksString"
            )
            requireContext().startActivity(this)
        }

    }


    private fun bindOnClick() = with(binding) {
        toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        ibDetails.setOnClickListener {
            makeBottomSheetDialog().show()
        }

    }

    private fun makeDeleteTrackDialog(track: Track): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            ContextThemeWrapper(
                requireContext(),
                R.style.DialogTheme
            )
        )
            .setTitle(getString(R.string.want_to_delete_track_))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteTrack(track)
            }
    }

    private fun makeDeletePlaylistDialog(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            ContextThemeWrapper(
                requireContext(),
                R.style.DialogTheme
            )
        )
            .setTitle(getString(R.string.want_to_delete_playlist_))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
    }

    private fun makeSnackbar(view: View): Snackbar {
        return Snackbar.make(
            ContextThemeWrapper(requireContext(), R.style.CustomSnackbarTheme),
            view,
            getString(R.string.no_tracks_for_share),
            Snackbar.LENGTH_LONG
        )
    }

    private fun makeBottomSheetDialog(): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialogTheme
        )
        val binding =
            BottomSheetPlaylistDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(binding.root)

        val currentPlaylist = viewModel.getCurrentPlaylist()
        val currentTracks = viewModel.getCurrentTracks()
        with(binding) {
            Glide.with(requireView()).load(viewModel.getFileImageFromStorage())
                .placeholder(R.drawable.track_placeholder).centerCrop()
                .into(ivPlaylistBottomSheet)

            tvPlaylistNameBottomSheet.text = currentPlaylist.name

            tvTracksCountBottomSheet.text = requireContext().resources.getQuantityString(
                R.plurals.plurals_track, currentTracks.size, currentTracks.size
            )

            tvShare.setOnClickListener {
                if (currentTracks.isEmpty()) bottomSheetDialog.dismiss()
                makeConditionShareIntent(currentPlaylist, currentTracks)
            }

            tvEditInfo.setOnClickListener {
                bottomSheetDialog.dismiss()
                findNavController().navigate(
                    R.id.action_playlistFragment_to_editPlaylistFragment,
                    EditPlaylistFragment.createArgs(viewModel.getTrackId())
                )
            }

            tvDeletePlaylist.setOnClickListener {
                bottomSheetDialog.dismiss()
                makeDeletePlaylistDialog().show()
            }
        }

        return bottomSheetDialog
    }

}