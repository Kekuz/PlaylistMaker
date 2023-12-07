package com.example.playlistmaker.domain.search.api.repository

import com.example.playlistmaker.domain.search.models.Resource

interface TrackRepository {
    fun search(term: String): Resource
}