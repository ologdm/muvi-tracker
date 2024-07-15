package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.example.muvitracker.data.detail.DetailEntity
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow

interface DetailRepo {

    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse2<DetailMovie>>

    fun getDetailListFlow(): Flow<List<DetailEntity>>

}