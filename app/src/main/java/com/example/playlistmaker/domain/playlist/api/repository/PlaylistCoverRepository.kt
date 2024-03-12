package com.example.playlistmaker.domain.playlist.api.repository

interface PlaylistCoverRepository {
    fun getImageFromPrivateStorage(fileName: String): String

    fun saveImageToPrivateStorage(uri: String, fileName: String)
}