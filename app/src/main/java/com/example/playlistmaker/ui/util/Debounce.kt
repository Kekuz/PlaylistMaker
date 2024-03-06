package com.example.playlistmaker.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debounce {
    private var isClickAllowed = true
    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            CoroutineScope(Dispatchers.IO).launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}