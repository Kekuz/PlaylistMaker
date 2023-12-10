package com.example.playlistmaker.domain.search.api.repository

import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track

interface TrackRepository {
    fun search(term: String): Resource<List<Track>>
}