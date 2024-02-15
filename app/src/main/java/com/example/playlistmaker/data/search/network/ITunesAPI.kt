package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.network.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("search?entity=song")
    suspend fun search(@Query("term") term: String): TrackSearchResponse
}