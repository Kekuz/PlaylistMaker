package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylists(): List<Playlist> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun getPlaylistById(id: Int): Playlist? {
        return playlistRepository.getPlaylistById(id)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun getTracksFromPlaylistByIds(ids: List<String>): List<Track> {
        return playlistRepository.getTracksFromPlaylistByIds(ids)
    }

}