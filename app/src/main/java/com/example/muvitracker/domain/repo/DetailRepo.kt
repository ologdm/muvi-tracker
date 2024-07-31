package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.database.entities.DetailEntityR
import com.example.muvitracker.domain.model.DetailMovie

import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow

interface DetailRepo {

    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse2<DetailMovie>>

    fun getDetailListFlow(): Flow<List<DetailEntityR?>>

}