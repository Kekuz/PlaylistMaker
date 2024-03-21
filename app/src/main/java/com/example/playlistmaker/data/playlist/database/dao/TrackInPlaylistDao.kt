package com.example.playlistmaker.data.playlist.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.playlist.database.model.TrackInPlaylistDatabaseEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(track: TrackInPlaylistDatabaseEntity)

    @Query("SELECT * FROM track_in_playlist_database")
    fun getAll(): List<TrackInPlaylistDatabaseEntity>

    @Delete
    fun delete(track: TrackInPlaylistDatabaseEntity)

}