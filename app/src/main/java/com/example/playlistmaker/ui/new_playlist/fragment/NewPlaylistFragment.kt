package com.example.playlistmaker.ui.new_playlist.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.playlist.model.Playlist
import com.example.playlistmaker.ui.new_playlist.view_model.NewPlaylistViewModel
import com.example.playlistmaker.ui.search.fragment.TrackViewHolder
import com.example.playlistmaker.ui.util.Convert
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var uri: Uri? = null

    private val viewModel by viewModel<NewPlaylistViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO сделать диалог с заголовком «Завершить создание плейлиста?»
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    //binding.picture.setImageURI(uri)
                    Glide.with(view).load(uri)
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
                    this.uri = uri
                    //saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на кнопку pickImage запускаем photo picker
        binding.ivAddPicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //TODO сделать кнопку серой, если имя пустое
        binding.btnCreate.setOnClickListener {
            uri?.let { saveImageToPrivateStorage(it, binding.name.text.toString()) }
            viewModel.createPlaylist(
                Playlist(
                    binding.name.text.toString(),
                    binding.description.text.toString(),
                    "playlist/${binding.name.text.toString()}.jpg"
                )
            )
            findNavController().navigateUp()
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.name.text.toString()} создан.",
                Toast.LENGTH_SHORT
            ).show()
        }
        //по нажатию на кнопку loadImageFromStorage пытаемся загрузить фотографию из нашего хранилища
        /*binding.loadImageFromStorage.setOnClickListener {
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myalbum"
            )
            val file = File(filePath, "first_cover.jpg")
            binding.storageImage.setImageURI(file.toUri())
        }*/
    }

    private fun saveImageToPrivateStorage(uri: Uri, fileName: String) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist")
        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$fileName.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    companion object {
        const val ROUNDED_CORNERS = 8f
    }
}