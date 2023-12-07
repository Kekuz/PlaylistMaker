package com.example.playlistmaker.domain.api.repository

import com.example.playlistmaker.domain.models.Resource

interface TrackRepository {
    fun search(term: String): Resource
}