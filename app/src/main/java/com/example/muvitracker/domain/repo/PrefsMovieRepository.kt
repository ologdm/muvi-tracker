package com.example.muvitracker.domain.repo

import com.example.muvitracker.domain.model.DetailMovie
import kotlinx.coroutines.flow.Flow

interface PrefsMovieRepository {

    //    fun getList(): LiveData<List<DetailMovie>>
    fun getListFLow(): Flow<List<DetailMovie>>

    suspend fun toggleLikedOnDB(movieId: Int)

    suspend fun updateWatchedOnDB(movieId: Int, watched: Boolean)

    suspend fun deleteItemOnDB(movieId: Int)

}