package com.example.playlistmaker.domain.api.repository

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun search(term: String): List<Track>
}