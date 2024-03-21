package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistDeletingInteractor
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository

class PlaylistDeletingInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistDeletingInteractor {
    override suspend fun deleteTrackFromPlaylist(id: String, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(id, playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }
}