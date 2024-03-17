package com.example.playlistmaker.ui.playlists.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.playlists.model.PlaylistState
import com.example.playlistmaker.ui.playlists.recycler.PlaylistAdapter
import com.example.playlistmaker.ui.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter(viewModel.getCoverRepository())

        binding.playlistRecycler.adapter = playlistAdapter

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }

        viewModel.getPlaylists()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlists)
            is PlaylistState.Empty -> showEmptyContent()
        }
    }

    private fun showEmptyContent() = with(binding) {
        emptyGroup.isVisible = true
        playlistRecycler.isVisible = false
        playlistAdapter.clearPlaylists()
    }

    private fun showContent(playlists: List<Playlist>) = with(binding) {
        playlistRecycler.isVisible = true
        emptyGroup.isVisible = false
        playlistAdapter.clearPlaylists()
        playlistAdapter.addPlaylists(playlists)
        playlistAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }
}