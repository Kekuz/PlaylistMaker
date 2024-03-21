package com.example.playlistmaker.domain.playlist.api.interactor

interface PlaylistCoverInteractor {
    fun getImageFromPrivateStorage(fileName: String?): String

    fun saveImageToPrivateStorage(uri: String, fileName: String)
}