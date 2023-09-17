package com.example.playlistmaker.search

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private companion object {
        const val TRACK_ARRAY_KEY = "track_array_key"
        const val TRACKS_IN_HISTORY = 10
    }

    private val gson by lazy { Gson() }

    var tracks = getTracksToPrefs()

    fun add(track: Track) {
        tracks.addToTrackArray(track)
        putTracksToPrefs(tracks)
    }

    fun clear(){
        tracks.clear()
        putTracksToPrefs(listOf())
    }


    private fun MutableList<Track>.addToTrackArray(track: Track) {
        var alreadyInside = false

        for (x in this){
            if(x == track) alreadyInside = true
        }

        if (alreadyInside) remove(track)

        if (size >= TRACKS_IN_HISTORY) removeAt(0)

        add(track)
    }

    private fun getTracksToPrefs(): MutableList<Track> {
        val json = sharedPreferences.getString(TRACK_ARRAY_KEY, "") ?: ""

        val itemsListType: Type = object : TypeToken<MutableList<Track?>?>() {}.type

        return gson.fromJson(json, itemsListType) ?: mutableListOf()
    }

    private fun putTracksToPrefs(tracks: List<Track>) {
        val json = gson.toJson(tracks)

        sharedPreferences.edit()
            .putString(TRACK_ARRAY_KEY, json)
            .apply()
    }
}




















