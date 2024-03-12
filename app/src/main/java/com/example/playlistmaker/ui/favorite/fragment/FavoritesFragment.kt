package com.example.playlistmaker.ui.favorite.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.audioplayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.media.models.FavoritesState
import com.example.playlistmaker.ui.favorite.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.search.fragment.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoritesViewModel>()

    private val onClick: (Track) -> Unit =
        {
            if (viewModel.clickDebounce()) {
                if (it.previewUrl != "-") {
                    Log.d("Track opened", it.toString())
                    findNavController().navigate(
                        R.id.action_mediaFragment_to_audioPlayerFragment,
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

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks, onClick)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindAdapter()

        viewModel.showFavorites()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showEmpty()
            is FavoritesState.Content -> showContent(state.tracks)
        }
    }

    private fun showContent(content: List<Track>) = with(binding) {
        tracks.clear()
        tracks.addAll(content)
        trackAdapter.notifyDataSetChanged()
        favoriteRv.isVisible = true
        emptyGroup.isVisible = false
    }

    private fun showEmpty() = with(binding) {
        emptyGroup.isVisible = true
        favoriteRv.isVisible = false
    }

    private fun bindAdapter() = with(binding) {
        favoriteRv.adapter = trackAdapter
    }

    companion object {
        fun newInstance(): FavoritesFragment = FavoritesFragment()
    }
}