package com.example.muvitracker.domain.repo


import com.example.muvitracker.domain.model.DetailShow
import kotlinx.coroutines.flow.Flow

interface PrefsShowRepo {

    fun getListFLow(): Flow<List<DetailShow>>

    suspend fun toggleFavoriteOnDB(id: Int)

    suspend fun deleteItemOnDB(id: Int)

    // TODO read watched count from Episodes

}