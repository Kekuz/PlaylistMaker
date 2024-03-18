package com.example.playlistmaker.ui.playlists.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.playlists.model.PlaylistsState
import com.example.playlistmaker.ui.playlists.recycler.PlaylistsAdapter
import com.example.playlistmaker.ui.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var playlistsAdapter: PlaylistsAdapter

    private val onClick: (Playlist) -> Unit = {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistFragment,
            bundleOf("id" to it.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsAdapter = PlaylistsAdapter(viewModel.getCoverRepository(), onClick)

        binding.playlistRecycler.adapter = playlistsAdapter

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }

        viewModel.getPlaylists()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.Empty -> showEmptyContent()
        }
    }

    private fun showEmptyContent() = with(binding) {
        emptyGroup.isVisible = true
        playlistRecycler.isVisible = false
        playlistsAdapter.clearPlaylists()
    }

    private fun showContent(playlists: List<Playlist>) = with(binding) {
        playlistRecycler.isVisible = true
        emptyGroup.isVisible = false
        playlistsAdapter.clearPlaylists()
        playlistsAdapter.addPlaylists(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }
}