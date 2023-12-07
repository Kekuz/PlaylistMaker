package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.dto.TrackResponse
import com.example.playlistmaker.data.search.network.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitNetworkClient: NetworkClient {
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)

    override fun doRequest(dto: Any): TrackResponse {
        try {
            if (dto is TrackSearchRequest) {
                val resp = iTunesService.search(dto.term).execute()

                val body = resp.body() ?: TrackResponse()

                return body.apply { resultCode = 200 }
            } else {
                return TrackResponse().apply { resultCode = 400 }
            }
        }catch (e: Exception){
            return TrackResponse().apply { resultCode = 404 }
        }

    }

}