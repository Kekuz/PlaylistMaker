package com.example.playlistmaker.data.playlist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.playlist.database.model.PlaylistDatabaseEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: PlaylistDatabaseEntity)

    @Query("SELECT * FROM playlist_database")
    fun getAll(): List<PlaylistDatabaseEntity>

}