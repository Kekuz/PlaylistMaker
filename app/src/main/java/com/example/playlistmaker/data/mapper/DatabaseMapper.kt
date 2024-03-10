package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity
import com.example.playlistmaker.data.playlist.database.model.PlaylistDatabaseEntity
import com.example.playlistmaker.domain.playlist.model.Playlist
import com.example.playlistmaker.domain.search.models.Track

object DatabaseMapper {

    fun map(track: Track): TrackDatabaseEntity = with(track) {
        return TrackDatabaseEntity(
            id = trackId,
            timeCreated = System.currentTimeMillis(),
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            artworkUrl100 = artworkUrl100,
            artworkUrl512 = artworkUrl512,
            collectionName = collectionName,
            releaseYear = releaseYear,
            primaryGenreName = primaryGenreName,
            country = country,
            previewUrl = previewUrl
        )
    }

    fun map(trackDatabaseEntity: TrackDatabaseEntity): Track = with(trackDatabaseEntity) {
        return Track(
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            artworkUrl100 = artworkUrl100,
            artworkUrl512 = artworkUrl512,
            trackId = id,
            collectionName = collectionName,
            releaseYear = releaseYear,
            primaryGenreName = primaryGenreName,
            country = country,
            timeCreated = timeCreated,
            previewUrl = previewUrl,
        )
    }

    fun map(playlist: Playlist): PlaylistDatabaseEntity = with(playlist) {
        return PlaylistDatabaseEntity(
            name = name,
            description = description,
            pathToCover = pathToCover,
            trackIdsList = trackIdsList,
            tracksCount = tracksCount,
        )
    }
}