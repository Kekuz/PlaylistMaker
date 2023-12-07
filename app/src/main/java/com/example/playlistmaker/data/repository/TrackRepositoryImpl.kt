package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.dto.TrackSearchRequest
import com.example.playlistmaker.data.network.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.repository.TrackRepository
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    private val dateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun search(term: String): Resource {
        val response = networkClient.doRequest(TrackSearchRequest(term))
        if (response.resultCode == 200) {
            return Resource(
                (response as TrackSearchResponse).results.map {
                    Track(
                        it.trackName ?: "no name",
                        it.artistName ?: "no artist",
                        dateFormat.format(it.trackTimeMillis ?: 0),
                        it.artworkUrl100 ?: "null",
                        it.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: "null",
                        it.trackId ?: 0,
                        it.collectionName ?: "-",
                        it.releaseDate?.substringBefore('-') ?: "-",//Передаем только год
                        it.primaryGenreName ?: "-",
                        it.country ?: "-",
                        it.previewUrl,
                    )
                }, response.resultCode
            )
        } else {
            return Resource(null, response.resultCode)
        }
    }
}