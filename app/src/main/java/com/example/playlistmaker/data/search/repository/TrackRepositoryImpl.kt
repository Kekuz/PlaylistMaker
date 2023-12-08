package com.example.playlistmaker.data.search.repository

import android.util.Log
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.dto.TrackSearchResponse
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
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
                        //В задании написано: "Показывать название альбома (collectionName) (если есть)"
                        //iTunes при отсутствии альбома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
                        checkSingle(it.collectionName),
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

    private fun checkSingle(string: String?): String {
        return if(string?.endsWith(NO_ALBUM_SUBSTRING) == true){
            "-"
        }else{
            string ?: "-"
        }

    }

    companion object {
        private const val NO_ALBUM_SUBSTRING = " - Single"
    }
}