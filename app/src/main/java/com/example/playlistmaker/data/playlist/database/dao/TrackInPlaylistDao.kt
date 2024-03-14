package com.example.playlistmaker.data.playlist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.data.playlist.database.model.TrackInPlaylistDatabaseEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(playlist: TrackInPlaylistDatabaseEntity)

}