package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryStorage {

    fun getTracks(): List<Track>

    fun putTracks(tracks: List<Track>)

}