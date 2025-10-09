package com.example.muvitracker.domain.repo


import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow

interface PrefsShowRepository {
    fun getListFLow(): Flow<List<DetailShow>>
    suspend fun toggleLikedOnDB(id: Int)
    suspend fun checkAndAddIfWatchedToPrefs(showId: Int)
    suspend fun deleteItemOnDB(id: Int)
}