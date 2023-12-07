package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryStorage {

    fun getTracks(): MutableList<Track>

    fun putTracks(tracks: List<Track>)

}