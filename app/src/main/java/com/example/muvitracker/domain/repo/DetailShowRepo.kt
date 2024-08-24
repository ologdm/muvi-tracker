package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.domain.model.DetailShow

import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

// 00
interface DetailShowRepo {

    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse<DetailShow>>

    fun getDetailListFlow(): Flow<List<DetailShowEntity?>>

}