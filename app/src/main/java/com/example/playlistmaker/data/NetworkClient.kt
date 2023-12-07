package com.example.playlistmaker.data

import com.example.playlistmaker.data.network.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}