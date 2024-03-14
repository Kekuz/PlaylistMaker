package com.example.playlistmaker.ui.new_playlist.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.databinding.SnackbarViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.new_playlist.view_model.NewPlaylistViewModel
import com.example.playlistmaker.ui.util.Convert
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                view?.let {
                    Glide.with(it).load(uri)
                        .centerCrop()
                        .transform(
                            RoundedCorners(
                                Convert.dpToPx(
                                    ROUNDED_CORNERS,
                                    requireContext()
                                )
                            )
                        )
                        .into(binding.picture)
                }
                this.uri = uri
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showDialogOrNavigateBack()
        }
    }

    private var uri: Uri? = null

    private val viewModel by viewModel<NewPlaylistViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.addTextChangedListener(getTextWatcher())
        bindOnClick(view)
    }

    private fun bindOnClick(view: View) = with(binding) {
        toolBar.setNavigationOnClickListener {
            showDialogOrNavigateBack()
        }

        ivAddPicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnCreate.setOnClickListener {
            uri?.let { viewModel.saveImage(it.toString(), binding.name.text.toString()) }
            viewModel.createPlaylist(
                Playlist(
                    name = binding.name.text.toString(),
                    description = binding.description.text.toString(),
                    pathToCover = "${binding.name.text.toString()}.jpg"
                )
            )

            findNavController().navigateUp()
            makeSnackbar(view, binding.name.text.toString()).show()

        }
    }

    private fun showDialogOrNavigateBack() {
        if (uri != null || binding.name.text.toString() != "" || binding.description.text.toString() != "") {
            makeDialog().show()
        } else {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun makeSnackbar(view: View, trackName: String): Snackbar {
        val customSnackbar = Snackbar.make(
            ContextThemeWrapper(requireContext(), R.style.CustomSnackbarTheme),
            view,
            "",
            Snackbar.LENGTH_LONG
        )
        val layout = customSnackbar.view as Snackbar.SnackbarLayout
        val bind: SnackbarViewBinding = SnackbarViewBinding.inflate(layoutInflater)

        layout.addView(bind.root, 0)
        bind.sbText.text =
            getString(R.string.playlist) + " $trackName " + getString(R.string.created_)
        return customSnackbar
    }

    private fun makeDialog(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.end_playlist_creation_))
            .setMessage(getString(R.string.all_unsaved_picks_lost))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.btnCreate.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.yp_text_gray)
                    binding.btnCreate.isEnabled = false
                } else {
                    binding.btnCreate.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.yp_blue)
                    binding.btnCreate.isEnabled = true
                }
            }
        }
    }

    companion object {
        const val ROUNDED_CORNERS = 8f
    }
}