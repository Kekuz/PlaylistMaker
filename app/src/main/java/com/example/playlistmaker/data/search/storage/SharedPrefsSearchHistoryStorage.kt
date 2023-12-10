package com.example.playlistmaker.data.search.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.search.SearchHistoryStorage
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPrefsSearchHistoryStorage(context: Context) : SearchHistoryStorage {

    private val sharedPreferences = context.getSharedPreferences(
        HISTORY_SHARED_PREFERENCES,
        MODE_PRIVATE
    )
    private val gson = Gson()

    override fun getTracks(): List<Track> {
        val json = sharedPreferences.getString(TRACK_ARRAY_KEY, "") ?: ""

        val itemsListType: Type = object : TypeToken<MutableList<Track?>?>() {}.type

        return gson.fromJson(json, itemsListType) ?: mutableListOf()
    }

    override fun putTracks(tracks: List<Track>) {
        val json = gson.toJson(tracks)

        sharedPreferences.edit()
            .putString(TRACK_ARRAY_KEY, json)
            .apply()
    }

    private companion object {
        const val HISTORY_SHARED_PREFERENCES = "history_shared_preferences"
        const val TRACK_ARRAY_KEY = "track_array_key"
    }
}