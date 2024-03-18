package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity
import com.example.playlistmaker.data.playlist.database.TrackInPlaylistDatabase
import com.example.playlistmaker.data.playlist.database.model.PlaylistDatabaseEntity
import com.example.playlistmaker.data.playlist.database.model.TrackInPlaylistDatabaseEntity
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

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

    fun mapPlaylistToDelete(playlist: Playlist): PlaylistDatabaseEntity = with(playlist) {
        return PlaylistDatabaseEntity(
            id = id,
            name = name,
            description = description,
            pathToCover = pathToCover,
            trackIdsList = trackIdsList,
            tracksCount = tracksCount,
        )
    }

    fun mapAndAddTrackToPlaylist(playlist: Playlist, track: Track): PlaylistDatabaseEntity =
        with(playlist) {
            val newTrackIdsList = mutableListOf<String>()
            newTrackIdsList.addAll(playlist.trackIdsList)
            newTrackIdsList.add(track.trackId.toString())

            return PlaylistDatabaseEntity(
                id = id,
                name = name,
                description = description,
                pathToCover = pathToCover,
                trackIdsList = newTrackIdsList,
                tracksCount = tracksCount + 1,
            )
        }

    fun mapAndDeleteTrackFromPlaylist(id: String, playlist: Playlist): PlaylistDatabaseEntity =
        with(playlist) {
            val newTrackIdsList = mutableListOf<String>()
            newTrackIdsList.addAll(playlist.trackIdsList)
            newTrackIdsList.remove(id)

            return PlaylistDatabaseEntity(
                id = this.id,
                name = name,
                description = description,
                pathToCover = pathToCover,
                trackIdsList = newTrackIdsList,
                tracksCount = tracksCount - 1,
            )
        }


    fun map(playlistDatabaseEntity: PlaylistDatabaseEntity): Playlist =
        with(playlistDatabaseEntity) {
            return Playlist(
                id = id,
                name = name,
                description = description,
                pathToCover = pathToCover,
                trackIdsList = trackIdsList,
                tracksCount = tracksCount,
            )
        }

    fun mapNullable(playlistDatabaseEntity: PlaylistDatabaseEntity?): Playlist? =
        with(playlistDatabaseEntity) {
            return if (this != null) {
                Playlist(
                    id = id,
                    name = name,
                    description = description,
                    pathToCover = pathToCover,
                    trackIdsList = trackIdsList,
                    tracksCount = tracksCount,
                )
            } else null
        }

    fun mapToPlaylist(track: Track): TrackInPlaylistDatabaseEntity = with(track) {
        return TrackInPlaylistDatabaseEntity(
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

    fun mapFromPlaylist(track: TrackInPlaylistDatabaseEntity): Track = with(track) {
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
}