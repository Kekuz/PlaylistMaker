package com.example.playlistmaker.data.favorites.mapper

import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity
import com.example.playlistmaker.domain.search.models.Track

object DatabaseMapper {

    fun map(track: Track): TrackDatabaseEntity = with(track) {
        return TrackDatabaseEntity(
            id = trackId,
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
            previewUrl = previewUrl,
        )
    }
}