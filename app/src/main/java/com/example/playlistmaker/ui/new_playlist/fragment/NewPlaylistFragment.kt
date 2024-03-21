package com.example.playlistmaker.ui.new_playlist.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.new_playlist.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID


open class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    protected open val binding get() = _binding!!


    protected val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                view?.let {
                    Glide.with(it).load(uri)
                        .centerCrop()
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

    private val inputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    }

    protected var uri: Uri? = null

    protected open val viewModel by viewModel<NewPlaylistViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

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
        bindTextWatcher()
        bindOnClick(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        inputMethodManager?.hideSoftInputFromWindow(
            view?.windowToken,
            0
        )

    }

    protected open fun bindOnClick(view: View) = with(binding) {
        toolBar.setNavigationOnClickListener {
            showDialogOrNavigateBack()
        }

        ivAddPicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnCreate.setOnClickListener {
            val pictureName = UUID.randomUUID().toString()
            uri?.let { viewModel.saveImage(it.toString(), pictureName) }
            viewModel.createPlaylist(
                Playlist(
                    name = name.text.toString(),
                    description = description.text.toString(),
                    pathToCover = "$pictureName.jpg"
                )
            )

            findNavController().navigateUp()
            makeSnackbar(view, name.text.toString()).show()

        }
    }

    private fun showDialogOrNavigateBack() {
        if (uri != null || binding.name.text.toString() != "" || binding.description.text.toString() != "") {
            makeDialog().show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun makeSnackbar(view: View, trackName: String): Snackbar {
        return Snackbar.make(
            ContextThemeWrapper(requireContext(), R.style.CustomSnackbarTheme),
            view,
            getString(R.string.playlist) + " $trackName " + getString(R.string.created_),
            Snackbar.LENGTH_LONG
        )
    }

    private fun makeDialog(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            ContextThemeWrapper(
                requireContext(),
                R.style.DialogTheme
            )
        )
            .setTitle(getString(R.string.end_playlist_creation_))
            .setMessage(getString(R.string.all_unsaved_picks_lost))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }
    }

    private fun bindTextWatcher() = with(binding) {
        name.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
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
        )
    }
}