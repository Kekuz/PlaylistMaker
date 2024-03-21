package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository

class PlaylistCoverInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistCoverInteractor {
    override fun getImageFromPrivateStorage(fileName: String?): String {
        return playlistRepository.getImageFromPrivateStorage(fileName)
    }

    override fun saveImageToPrivateStorage(uri: String, fileName: String) {
        playlistRepository.saveImageToPrivateStorage(uri, fileName)
    }
}