package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryStorage {

    suspend fun getTracks(): List<Track>

    fun putTracks(tracks: List<Track>)

}