package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.dto.TrackSearchRequest
import com.example.playlistmaker.data.network.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.repository.TrackRepository
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    private val dateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun search(term: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(term))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    dateFormat.format(it.trackTimeMillis),
                    it.artworkUrl100,
                    it.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
                    it.trackId,
                    it.collectionName,
                    it.releaseDate.substringBefore('-'),//Передаем только год
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl,
                )
            }
        } else {
            return emptyList()
        }
    }
}