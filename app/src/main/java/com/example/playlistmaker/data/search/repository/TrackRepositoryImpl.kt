package com.example.playlistmaker.data.search.repository

import android.content.Context
import android.util.Log
import com.example.playlistmaker.R
import com.example.playlistmaker.data.favorites.DatabaseClient
import com.example.playlistmaker.data.mapper.DatabaseMapper
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.dto.TrackSearchResponse
import com.example.playlistmaker.domain.search.api.repository.TrackRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val databaseClient: DatabaseClient,
    private val context: Context
) : TrackRepository {

    private val dateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun search(term: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(term))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.internet_problems)))
            }

            200 -> {
                Log.d("Tracks", (response as TrackSearchResponse).results.toString())
                emit(Resource.Success((response).results.map {
                    Track(
                        trackName = it.trackName ?: "no name",
                        artistName = it.artistName ?: "no artist",
                        trackTime = dateFormat.format(it.trackTimeMillis ?: 0),
                        artworkUrl100 = it.artworkUrl100 ?: "null",
                        artworkUrl512 = it.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: "null",
                        trackId = it.trackId ?: 0,
                        //В задании написано: "Показывать название альбома (collectionName) (если есть)"
                        //iTunes при отсутствии альбома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
                        collectionName = checkSingle(it.collectionName),
                        releaseYear = it.releaseDate?.substringBefore('-') ?: "-",//Передаем только год
                        primaryGenreName = it.primaryGenreName ?: "-",
                        country = it.country ?: "-",
                        previewUrl = it.previewUrl ?: "-",
                        isFavorite = checkFavorite(it.trackId ?: -1),
                    )
                }))
            }

            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }

    private fun checkSingle(string: String?): String {
        return if (string?.endsWith(NO_ALBUM_SUBSTRING) == true) {
            "-"
        } else {
            string ?: "-"
        }

    }

    private suspend fun checkFavorite(id: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext databaseClient.getIds().contains(id)
    }

    override suspend fun save(track: Track) = withContext(Dispatchers.IO) {
        databaseClient.save(DatabaseMapper.map(track))
    }

    override suspend fun delete(track: Track) = withContext(Dispatchers.IO) {
        databaseClient.delete(DatabaseMapper.map(track))
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        emit(databaseClient.getTracks().map { DatabaseMapper.map(it) })
    }


    companion object {
        private const val NO_ALBUM_SUBSTRING = " - Single"
    }
}