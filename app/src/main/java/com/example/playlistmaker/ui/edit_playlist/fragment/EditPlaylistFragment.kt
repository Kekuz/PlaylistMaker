package com.example.playlistmaker.ui.edit_playlist.fragment

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.edit_playlist.model.EditPlaylistViewState
import com.example.playlistmaker.ui.edit_playlist.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.new_playlist.fragment.NewPlaylistFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.UUID

class EditPlaylistFragment : NewPlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistViewModel> {
        parametersOf(
            requireArguments().getInt(ARG_EDIT)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.binding.toolBar.title = getString(R.string.edit)
        super.binding.btnCreate.text = getString(R.string.save)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun bindOnClick(view: View) = with(binding) {
        toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        ivAddPicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnCreate.setOnClickListener {
            if (uri != null) {
                val pictureName = UUID.randomUUID().toString()
                viewModel.saveImage(uri.toString(), pictureName)

                viewModel.updatePlaylist(
                    name.text.toString(),
                    description.text.toString(),
                    "$pictureName.jpg"
                )
            } else {
                viewModel.updatePlaylist(
                    name.text.toString(),
                    description.text.toString(),
                )
            }

            findNavController().navigateUp()
            makeSnackbar(view, name.text.toString()).show()

        }
    }

    private fun render(state: EditPlaylistViewState) {
        when (state) {
            is EditPlaylistViewState.Content -> showContent(state.playlist)
        }
    }

    private fun showContent(playlist: Playlist) = with(binding) {
        name.setText(playlist.name)
        description.setText(playlist.description)
        Glide.with(requireView()).load(viewModel.getCoverFile())
            .centerCrop()
            .into(picture)
    }

    private fun makeSnackbar(view: View, trackName: String): Snackbar {

        return Snackbar.make(
            ContextThemeWrapper(requireContext(), R.style.CustomSnackbarTheme),
            view,
            getString(R.string.playlist) + " $trackName " + getString(R.string.saved_),
            Snackbar.LENGTH_LONG
        )
    }

    companion object {
        private const val ARG_EDIT = "edit"
        fun createArgs(id: Int): Bundle =
            bundleOf(ARG_EDIT to id)

    }
}