package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.dto.Response
import com.example.playlistmaker.data.network.dto.TrackSearchRequest
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

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchRequest) {
                val resp = iTunesService.search(dto.term).execute()

                val body = resp.body() ?: Response()

                return body.apply { resultCode = 200 }
            } else {
                return Response().apply { resultCode = 400 }
            }
        }catch (e: Exception){
            return Response().apply { resultCode = 404 }
        }

    }

}