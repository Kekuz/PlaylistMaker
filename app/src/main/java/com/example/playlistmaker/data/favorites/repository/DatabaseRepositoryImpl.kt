package com.example.playlistmaker.data.favorites.repository

import com.example.playlistmaker.data.favorites.DatabaseClient
import com.example.playlistmaker.data.favorites.mapper.DatabaseMapper
import com.example.playlistmaker.domain.favorites.api.repository.DatabaseRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseRepositoryImpl(private val databaseClient: DatabaseClient) : DatabaseRepository {
    override suspend fun save(track: Track) {
        CoroutineScope(Dispatchers.IO).launch {
            databaseClient.save(DatabaseMapper.map(track))
        }
    }

    override suspend fun delete(track: Track) {
        CoroutineScope(Dispatchers.IO).launch {
            databaseClient.delete(DatabaseMapper.map(track))
        }
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        emit(databaseClient.getTracks().map { DatabaseMapper.map(it) })
    }


}