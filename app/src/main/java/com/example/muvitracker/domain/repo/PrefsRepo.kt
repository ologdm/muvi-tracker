package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.example.muvitracker.domain.model.DetailMovie
import kotlinx.coroutines.flow.Flow

interface PrefsRepo {

//    fun getList(): LiveData<List<DetailMovie>>
    fun getListFLow(): Flow<List<DetailMovie>>


    fun toggleFavoriteOnDB(id: Int)

    fun updateWatchedOnDB(id: Int, watched: Boolean)

    fun deleteItemOnDB(movieId: Int)

}