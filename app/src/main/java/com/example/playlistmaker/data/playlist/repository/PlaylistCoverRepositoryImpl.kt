package com.example.playlistmaker.data.playlist.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistCoverRepository
import java.io.File
import java.io.FileOutputStream

class PlaylistCoverRepositoryImpl(private val context: Context) : PlaylistCoverRepository {
    override fun getImageFromPrivateStorage(fileName: String): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
        val file = File(filePath, "$fileName.jpg")
        return file.toUri().toString()
    }

    override fun saveImageToPrivateStorage(uri: String, fileName: String) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$fileName.jpg")
        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    companion object {
        const val DIRECTORY = "playlist"
    }
}