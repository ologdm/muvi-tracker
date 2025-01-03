package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie

import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface DetailMovieRepo {

    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse<DetailMovie>>

    fun getDetailListFlow(): Flow<List<DetailMovieEntity?>>

    suspend fun getRelatedMovies(movieId: Int): IoResponse<List<Movie>>

}