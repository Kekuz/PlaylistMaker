package com.example.playlistmaker.data.favorites.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.favorites.database.model.TrackDatabaseEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: TrackDatabaseEntity)

    @Delete
    fun delete(track: TrackDatabaseEntity)

    @Query("SELECT * FROM track_database")
    fun get(): List<TrackDatabaseEntity>

    @Query("SELECT id FROM track_database")
    fun getIds(): List<Int>

}