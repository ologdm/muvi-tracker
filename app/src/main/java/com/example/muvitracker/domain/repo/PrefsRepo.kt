package com.example.muvitracker.domain.repo

import com.example.muvitracker.domain.model.DetailMovie
import kotlinx.coroutines.flow.Flow

interface PrefsRepo {

    //    fun getList(): LiveData<List<DetailMovie>>
    fun getListFLow(): Flow<List<DetailMovie>>

    suspend fun toggleLikedOnDB(id: Int)

    suspend fun updateWatchedOnDB(id: Int, watched: Boolean)

    suspend fun deleteItemOnDB(id: Int)

}