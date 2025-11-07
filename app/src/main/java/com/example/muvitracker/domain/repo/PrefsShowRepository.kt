package com.example.muvitracker.domain.repo


import com.example.muvitracker.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface PrefsShowRepository {
    fun getListFLow(): Flow<List<Show>>
    suspend fun toggleLikedOnDB(id: Int)
    suspend fun checkAndAddIfWatchedToPrefs(showId: Int)
    suspend fun deleteItemOnDB(id: Int)

    suspend fun setNotesOnDB(showId: Int, note: String)
}