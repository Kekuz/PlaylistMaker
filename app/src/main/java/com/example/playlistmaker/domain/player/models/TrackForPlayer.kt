package com.example.playlistmaker.domain.player.models

import com.example.playlistmaker.domain.search.models.Track

object TrackForPlayer {
    private var trackToOpenPlayerActivity: Track? = null

    fun init(newTrack: Track) {
        trackToOpenPlayerActivity = newTrack
    }

    fun get(): Track? = trackToOpenPlayerActivity
}