package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.dto.TrackResponse
import com.example.playlistmaker.data.search.network.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitNetworkClient(
    private val context: Context,
    private val iTunesService: ITunesAPI,
): NetworkClient {
    override fun doRequest(dto: Any): TrackResponse {
        if (!isConnected()) {
            return TrackResponse().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return TrackResponse().apply { resultCode = 400 }
        }
        return try {
            val resp = iTunesService.search(dto.term).execute()
            val body = resp.body() ?: TrackResponse()
            body.apply {
                resultCode = resp.code()
            }
        } catch (e:Exception){
            TrackResponse().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}