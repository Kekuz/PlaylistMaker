package com.example.playlistmaker.ui.search.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface SearchState {
    object Loading : SearchState
    object Empty: SearchState
    data class Content(
        val tracks: List<Track>
    ) : SearchState
    data class Error(
        val errorMessage: String
    ) : SearchState


}